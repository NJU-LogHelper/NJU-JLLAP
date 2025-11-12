# JLLAP (Java Log Lint and Analysis Plugin)

## 📖 Introduction

This project is an implementation of JLLAP (Java Log Lint and Analysis Plugin) for failure cause localization through standardizing logging practices and analyzing log files with the aid of a goal model.

Logs capture the dynamic behavior of software systems at runtime and provide critical information for software analysis, such as issue tracking and performance monitoring. The quality of these logs, however, depends entirely on the appropriate placement of logging statements. "Where to log?" and "What to log?" are two of the most significant challenges in logging practice, often relying on a developer's personal skill and preference, which inevitably leads to inconsistent log quality.

In large-scale projects, recognized best practices are easily neglected.

JLLAP aims to solve this problem. It is an IntelliJ IDEA plugin for Java that guides developers to insert informative logging statements in a convenient manner. Through real-time static analysis, the plugin helps developers identify critical locations missing logs during the coding phase and provides one-click generation and insertion.

## ✨ Main Features

### 1. Log Lint

JLLAP provides an on-the-fly check for missing logging statements based on a built-in set of rules that reflect commonly recognized logging practices.

* **Real-time Highlighting**: The plugin continuously scans code as the developer types, highlighting potential issues (e.g., `catch` blocks, `Thread.start()` calls).
* **Quickfix Insertion**: When an issue is detected, a "lightbulb" action is provided. The developer can choose their preferred logging framework (Log4j, SLF4j, java.util.logging) and log level (Info, Error, etc.). The plugin then automatically inserts a well-formed log statement.
* **Dynamic Templates**: The inserted log statement is not static; it is dynamically generated based on the current code context to automatically include relevant information such as class name, method name, and parameters.

### 2. Log Analysis

JLLAP is able to convert log files into a tree-structured goal model with status for further failure cause localization.

## 🔬 Log Lint: Core Inspection Rules

The "Log Lint" feature is built upon eight core inspection rules that cover the most common and critical logging scenarios:

1.  **Exception Check**: Detects `catch` blocks and prompts the developer to log the exception.
2.  **Assert Check**: Prompts for logging after `assert` statements.
3.  **Important Branch Check**: Recommends logging the program's execution path in key `if-else` or `switch` branches.
4.  **Thread-related Check**: Automatically detects and flags calls to critical thread methods, including `Thread.start()`, `Thread.run()`, and `Thread.join()`.
5.  **Database-related Check**:
    * Monitors JDBC connection operations like `DriverManager.getConnection()`, recommending a log entry for the connection event.
    * Monitors SQL execution operations like `Statement.executeQuery()` and `Statement.executeUpdate()`, recommending a log entry for the SQL string being executed.
6.  **Critical Class & Method Check (User-Configurable)**:
    * This feature allows teams to **define their own** set of critical classes and methods to monitor (supports regular expressions).
    * The plugin will then flag all calls to these configured methods.
    * The Quickfix for this rule intelligently inserts a log at the **entry** of the method (to record parameters) and **before every `return` statement** (to record return values).
7.  **File-related Check**: Recommends logging for file I/O operations.
8.  **Server-related Check**: Recommends logging for key network and server-side operations.

## 🛠️ Core Technology

The plugin's "Log Lint" feature is built on the following IntelliJ Platform APIs:

* **IntelliJ Inspection API**: This is the core of JLLAP's real-time analysis. Each logging rule is implemented as a `LocalInspectionTool`. This allows the plugin to define custom code-checking logic, which the IDE runs in the background to highlight problems in the editor.
* **PSI (Program Structure Interface)**: The PSI is how the IntelliJ Platform understands code. It parses source code into an "Abstract Syntax Tree" (AST). The plugin uses a `Visitor` pattern to traverse this PSI tree, looking for specific code structures (e.g., a `PsiMethodCallExpression` matching `Thread.start()`, or a `PsiTryStatement` to inspect its `catch` blocks).
* **Quickfix API**: When an `Inspection` finds a problem, a `Quickfix` (implementing `LocalQuickFix`) provides the "lightbulb" solution. When clicked, its `applyFix` method uses a `PsiElementFactory` to create new log code (as PSI elements) and inserts them precisely into the PSI tree at the correct location (e.g., the first line of a method body or just before a `return` statement).

## 🎓 Academic Publication

The rule set, recommendation engine, and implementation methods used in this tool are based on published academic research. This work (JLLAR) was accepted into the **Internetware '19** (a CCF-C conference).

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

## 🚀 Getting Started

If you would not like to build this plugin by yourself, you can get the package from the [release page](https://github.com/NJU-LogHelper/NJU-LogHelper/releases).

If you want to build it yourself, you have to make sure the following build environment:

* JDK 1.8 +
* Intellij IDEA 172.4343.14
* Python 2.7