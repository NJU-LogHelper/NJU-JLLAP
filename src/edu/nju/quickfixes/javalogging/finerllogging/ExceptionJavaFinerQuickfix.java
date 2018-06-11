package edu.nju.quickfixes.javalogging.finerllogging;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import edu.nju.util.loggingutil.CriticalOpeLoggingUtil;
import edu.nju.util.loggingutil.ExceptionLoggingUtil;
import edu.nju.util.loggingutil.LoggingType;
import edu.nju.util.loggingutil.logginglevel.JavaLoggingLevel;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class ExceptionJavaFinerQuickfix implements LocalQuickFix {
    @Nls
    @NotNull
    @Override
    public String getName() {
        return "Logging exception using java.util.logging finer level";
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return getName();
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
        ExceptionLoggingUtil.doExceptionLogging(project,problemDescriptor, LoggingType.JAVALOGGING, JavaLoggingLevel.LOG_FINER,0);
    }
}
