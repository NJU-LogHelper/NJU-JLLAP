package edu.nju.quickfixes.javalogging.finestlogging;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import edu.nju.util.loggingutil.LoggingType;
import edu.nju.util.loggingutil.CriticalOpeLoggingUtil;
import edu.nju.util.loggingutil.logginglevel.JavaLoggingLevel;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by chentiange on 2018/5/9.
 */
public class CriticalOpeJavaFinestQuickfix implements LocalQuickFix {
    @Nls
    @NotNull
    @Override
    public String getName() {
        return "Logging critical operation using java.util.logging finest level";
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return getName();
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
        CriticalOpeLoggingUtil.doCriticalOpeLogging(project,problemDescriptor, LoggingType.JAVALOGGING, JavaLoggingLevel.LOG_FINEST,0);
    }
}
