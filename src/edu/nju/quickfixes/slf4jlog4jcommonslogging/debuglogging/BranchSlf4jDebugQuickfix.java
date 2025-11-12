package edu.nju.quickfixes.slf4jlog4jcommonslogging.debuglogging;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import edu.nju.util.loggingutil.BranchStatementLoggingUtil;
import edu.nju.util.loggingutil.CriticalOpeLoggingUtil;
import edu.nju.util.loggingutil.LoggingType;
import edu.nju.util.loggingutil.logginglevel.Slf4JLoggingLevel;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * 分支语句的 SLF4J Debug 级别日志快速修复类
 * 用于在分支语句中自动添加 debug 级别的日志记录
 */
public class BranchSlf4jDebugQuickfix  implements LocalQuickFix {
    @Nls
    @NotNull
    @Override
    public String getName() {
        return "Logging branch using slf4j or log4j or commons-logging debug level";
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return getName();
    }

    /**
     * 应用快速修复，在分支语句中插入 debug 级别的日志
     * @param project 当前项目
     * @param problemDescriptor 问题描述符
     */
    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
        BranchStatementLoggingUtil.doBranchLogging(project,problemDescriptor, LoggingType.SLF4J, Slf4JLoggingLevel.LOG_DEBUG,2);
    }
}
