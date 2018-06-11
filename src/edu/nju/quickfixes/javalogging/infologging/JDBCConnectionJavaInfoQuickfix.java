package edu.nju.quickfixes.javalogging.infologging;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import edu.nju.util.loggingutil.JDBCConnectionLoggingUtil;
import edu.nju.util.loggingutil.logginglevel.JavaLoggingLevel;
import edu.nju.util.loggingutil.LoggingType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by chentiange on 2018/5/6.
 */
public class JDBCConnectionJavaInfoQuickfix implements LocalQuickFix {
    @Nls
    @NotNull
    @Override
    public String getName() {
        return "Logging JDBC connection using java.util.logging info level";
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return getName();
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
        JDBCConnectionLoggingUtil.doJDBCLogging(project,problemDescriptor, LoggingType.JAVALOGGING, JavaLoggingLevel.LOG_INFO,0);
    }

}
