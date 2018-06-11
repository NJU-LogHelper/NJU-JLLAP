package edu.nju.quickfixes.log4jcommonslogging.fatallogging;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import edu.nju.util.loggingutil.CriticalOpeLoggingUtil;
import edu.nju.util.loggingutil.ExceptionLoggingUtil;
import edu.nju.util.loggingutil.LoggingType;
import edu.nju.util.loggingutil.logginglevel.Log4jLoggingLevel;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class ExceptionLog4jFatalQuickfix implements LocalQuickFix {
    @Nls
    @NotNull
    @Override
    public String getName() {
        return "Logging exception using log4j or commons-logging fatal level";
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return getName();
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
        ExceptionLoggingUtil.doExceptionLogging(project,problemDescriptor, LoggingType.LOG4J, Log4jLoggingLevel.LOG_FATAL,1);
    }
}
