# JLLAP (Java 日志规范与分析插件)

## 📖 项目简介

本项目是 JLLAP (Java Log Lint and Analysis Plugin) 的一个实现。其目标是通过规范日志实践和借助“目标模型”(Goal Model) 分析日志文件，来实现故障定位。

日志是软件系统在运行时响应事件的执行记录，它能捕获系统的动态行为，为软件分析（如问题追踪、性能监控等）提供关键信息。然而，日志的质量严重依赖于日志语句的安插位置。"在哪里记录" 和 "记录什么内容" 是软件开发中日志实践的两大核心难题，而这些决策往往依赖于开发者的个人技能和偏好，不可避免地导致了日志质量参差不齐。

尤其在大型项目中，公认的最佳日志实践很容易被开发者忽略。

JLLAP 旨在解决这一问题。它是一款基于 IntelliJ IDEA 平台的 Java 插件，它引导开发人员以便捷的方式插入信息丰富的日志语句。插件通过实时静态分析，帮助开发者在编码阶段就识别出缺失日志的关键位置，并提供一键生成和插入功能。

## ✨ 主要功能

### 1. 日志规范检查 (Log Lint)

JLLAP 基于一套内置的、反映公认日志实践的规则集，为缺失的日志语句提供动态即时检查 (on-the-fly check)。

* **实时高亮提示**: 插件在开发人员编码时持续扫描代码，对检查项（如 `catch` 块、`Thread.start()` 调用等）进行高亮提示。
* **一键插入 (Quickfix)**: 当检测到问题时，提供 "小灯泡" 快捷操作。开发者可选择不同的日志框架 (Log4j, SLF4j, java.util.logging) 和日志级别 (Info, Error, Debug 等)，插件会自动在当前上下文插入一条规范的日志语句。
* **动态模板生成**: 插入的日志语句并非静态字符串，而是会根据当前代码环境动态生成，自动包含类名、方法名、参数等关键信息。

### 2. 日志分析 (Log Analysis)

JLLAP 能够将日志文件转换为带有状态的树状“目标模型”，以便进行进一步的故障原因定位。

## 🔬 Log Lint: 核心检查规则

插件的“日志规范检查”功能基于八条核心规则实现，这些规则覆盖了日志记录中最常见和最关键的场景：

1.  **Exception 检查**: 检测 `catch` 块，提示开发者必须在此处记录异常信息。
2.  **Assert 检查**: 提示在 `assert` 语句后记录断言失败的可能。
3.  **重要分支语句检查**: 提示在关键的 `if-else` 或 `switch` 逻辑分支中记录程序走向。
4.  **线程相关检查**: 自动检测并提示对关键线程方法（如 `Thread.start()`, `Thread.run()` 和 `Thread.join()`）的调用。
5.  **数据库相关检查**:
    * 监控 JDBC 的连接操作，如 `DriverManager.getConnection()`，提示记录数据库连接事件。
    * 监控 SQL 的执行操作，如 `Statement.executeQuery()` 和 `Statement.executeUpdate()`，提示记录被执行的 SQL 语句。
6.  **重要类与方法检查 (用户可配)**:
    * 此功能允许开发团队**自定义**需要监控的重要类和方法（支持正则表达式）。
    * 配置后，插件会自动提示对这些方法的调用。
    * (Quickfix) 插入日志时，插件会自动在方法体的**第一行**插入参数日志，并在所有 `return` 语句**之前**插入返回值日志。
7.  **文件相关检查**: 提示记录文件 I/O 操作。
8.  **服务器相关检查**: 提示记录网络和服务器相关的关键操作。

## 🛠️ 核心技术

插件的“Log Lint”功能（即日志规范检查）基于以下 IntelliJ 平台 API 构建:

* **IntelliJ Inspection API**: 这是实现 JLLAP 实时检查的核心。每条日志规则都被实现为一个 `LocalInspectionTool`。它允许插件定义自己的代码检查逻辑，IntelliJ 平台会自动在后台运行这些检查，并在编辑器中高亮显示问题。
* **PSI (Program Structure Interface)**: PSI 是 IntelliJ 平台理解代码的方式。它将源代码解析为一个可操作的语法树。插件通过 `Visitor` (访问者) 模式遍历 PSI 树，以查找特定的代码结构（例如，一个 `PsiMethodCallExpression` 来匹配 `Thread.start()` 调用，或一个 `PsiTryStatement` 来检查 `catch` 块）。
* **Quickfix API**: 当 `Inspection` 发现问题时，`Quickfix` (实现 `LocalQuickFix` 接口) 提供了 "小灯泡" 形式的解决方案。用户点击后, 插件会执行 `applyFix` 方法，通过 `PsiElementFactory` 创建新的日志代码（PSI 元素），并将其精确地插入到 PSI 树的正确位置（例如方法体的首行或 `return` 语句之前）。

## 🎓 学术成果 (Academic Publication)

本工具中使用的规则集、推荐引擎和实现方法基于已发表的学术研究。这项工作（JLLAR）被 **Internetware '19** (CCF-C 类会议) 接收为论文。

```latex
@inproceedings{10.1145/3361242.3361261,
author = {Zhu, Jing and Rong, Guoping and Huang, Guocheng and Gu, Shenghui and Zhang, He and Shao, Dong},
title = {JLLAR: A Logging Recommendation Plug-in Tool for Java},
year = {2019},
isbn = {9781450377010},
publisher = {Association for Computing Machinery},
address = {New York, NY, USA},
url = {[https://doi.org/10.1145/3361242.3361261](https://doi.org/10.1145/3361242.3361261)},
doi = {10.1145/3361242.3361261},
abstract = {Logs are the execution results of logging statements in software systems after being triggered by various events, which is able to capture the dynamic behavior of software systems during runtime and provide important information for software analysis, e.g., issue tracking, performance monitoring, etc. Obviously, to meet this purpose, the quality of the logs is critical, which requires appropriately placement of logging statements. Existing research on this topic reveals that where to log? and what to log? are two most concerns when conducting logging practice in software development, which mainly relies on developers' personal skills, expertise and preference, rendering several problems impacting the quality of the logs inevitably. One of the reasons leading to this phenomenon might be that several recognized best practices(strategies as well) are easily neglected by software developers. Especially in those software projects with relatively large number of participants. To address this issue, we designed and implemented a plug-in tool (i.e., JLLAR) based on the Intellij IDEA, which applied machine learning technology to identify and create a set of rules reflecting commonly recognized logging practices. Based on this rule set, JLLAR can be used to scan existing source code to identify issues regarding the placement of logging statements. Moreover, JLLAR also provides automatic code completion and semi code completion (i.e., to provide recommendations) regarding logging practice to support software developers during coding.},
booktitle = {Proceedings of the 11th Asia-Pacific Symposium on Internetware},
articleno = {16},
numpages = {6},
keywords = {tool, machine learning, logging practice},
location = {Fukuoka, Japan},
series = {Internetware '19}
}
```

## 🚀 快速上手

如果您不想自行构建, 可以从 [release 页面](https://github.com/NJU-LogHelper/NJU-LogHelper/releases) 获取插件包。

如果您希望自行构建, 请确保以下构建环境:

* JDK 1.8 +
* Intellij IDEA 172.4343.14
* Python 2.7