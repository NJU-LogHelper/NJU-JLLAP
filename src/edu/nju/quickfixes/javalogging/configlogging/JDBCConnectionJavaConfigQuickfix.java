package edu.nju.quickfixes.javalogging.configlogging;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import edu.nju.util.loggingutil.JDBCConnectionLoggingUtil;
import edu.nju.util.loggingutil.LoggingType;
import edu.nju.util.loggingutil.logginglevel.JavaLoggingLevel;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by chentiange on 2018/5/9.
 */
public class JDBCConnectionJavaConfigQuickfix implements LocalQuickFix {
    @Nls
    @NotNull
    @Override
    public String getName() {
        return "Logging JDBC connection using java.util.logging config level";
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return getName();
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
        JDBCConnectionLoggingUtil.doJDBCLogging(project,problemDescriptor, LoggingType.JAVALOGGING, JavaLoggingLevel.LOG_CONFIG,0);
    }
}
