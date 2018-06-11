package edu.nju.quickfixes.slf4jlog4jcommonslogging.infologging;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import edu.nju.util.loggingutil.ExceptionLoggingUtil;
import edu.nju.util.loggingutil.LoggingType;
import edu.nju.util.loggingutil.SwitchLoggingUtil;
import edu.nju.util.loggingutil.logginglevel.Slf4JLoggingLevel;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class SwitchSlf4jInfoQuickfix implements LocalQuickFix {
    @Nls
    @NotNull
    @Override
    public String getName() {
        return "Logging switch using slf4j or log4j or commons-logging info level";
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return getName();
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
        SwitchLoggingUtil.doSwitchLogging(project,problemDescriptor, LoggingType.SLF4J, Slf4JLoggingLevel.LOG_INFO,2);
    }
}
