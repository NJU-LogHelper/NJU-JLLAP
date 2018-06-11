package edu.nju.quickfixes.slf4jlog4jcommonslogging.debuglogging;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import edu.nju.util.loggingutil.JDBCExecuteLoggingUtil;
import edu.nju.util.loggingutil.LoggingType;
import edu.nju.util.loggingutil.logginglevel.Slf4JLoggingLevel;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by chentiange on 2018/5/13.
 */
public class JDBCUpdateSlf4jDebugQuickfix implements LocalQuickFix {
    @Nls
    @NotNull
    @Override
    public String getName() {
        return "Logging JDBC excute update using slf4j or log4j or commons-logging debug level";
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return getName();
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
        JDBCExecuteLoggingUtil.doJDBCLogging(project,problemDescriptor, LoggingType.SLF4J, Slf4JLoggingLevel.LOG_DEBUG,2);
    }
}
