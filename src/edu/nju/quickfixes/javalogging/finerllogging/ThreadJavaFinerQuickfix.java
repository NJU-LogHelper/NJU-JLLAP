package edu.nju.quickfixes.javalogging.finerllogging;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import edu.nju.util.loggingutil.logginglevel.JavaLoggingLevel;
import edu.nju.util.loggingutil.LoggingType;
import edu.nju.util.loggingutil.ThreadLoggingUtil;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by chentiange on 2018/5/5.
 */
public class ThreadJavaFinerQuickfix implements LocalQuickFix {
    @Nls
    @NotNull
    @Override
    public String getName() {
        return "Logging thread using java.util.logging finer level";
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return getName();
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
        ThreadLoggingUtil.doThreadLogging(project,problemDescriptor, LoggingType.JAVALOGGING, JavaLoggingLevel.LOG_FINER,0);
    }
}
