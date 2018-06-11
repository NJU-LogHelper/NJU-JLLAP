package edu.nju.quickfixes.javalogging.configlogging;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import edu.nju.util.loggingutil.CriticalOpeLoggingUtil;
import edu.nju.util.loggingutil.ExceptionLoggingUtil;
import edu.nju.util.loggingutil.LoggingType;
import edu.nju.util.loggingutil.logginglevel.JavaLoggingLevel;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class ExceptionJavaConfigQuickfix implements LocalQuickFix {
    @Nls
    @NotNull
    @Override
    public String getName() {
        return "Logging exception using java.util.logging config level";
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return getName();
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
       ExceptionLoggingUtil.doExceptionLogging(project,problemDescriptor, LoggingType.JAVALOGGING, JavaLoggingLevel.LOG_CONFIG,0);
    }
}
