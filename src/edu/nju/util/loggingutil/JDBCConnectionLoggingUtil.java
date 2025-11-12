package edu.nju.util.loggingutil;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

/**
 * Created by chentiange on 2018/5/6.
 * 为 JDBC 连接语句自动添加日志记录
 */
public class JDBCConnectionLoggingUtil {
    private static final Logger LOG = Logger.getInstance("#com.intellij.codeInspection.inspections.JDBCInspection");

    /**
     * 在 JDBC 获取连接处插入日志语句
     */
    public static void doJDBCLogging(@NotNull Project project,
                                     @NotNull ProblemDescriptor problemDescriptor,
                                     LoggingType type,
                                     Object level,
                                     int typeId) {
        try {
            // 取出形如 conn = DriverManager.getConnection(url, user, pwd);
            PsiAssignmentExpression assignmentExpression =
                    (PsiAssignmentExpression) problemDescriptor.getPsiElement();

            PsiMethodCallExpression rGetConn =
                    (PsiMethodCallExpression) assignmentExpression.getRExpression();
            PsiExpression[] args = rGetConn.getArgumentList().getExpressions();

            String url = args[0].getText();
            String user = args[1].getText();
            String password = args[2].getText();

            // 构造日志语句：log.xxx("JDBC connecting with url: "...)
            PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
            String levelStr = LoggingUtil.getLevelStringById(level, typeId);
            String logContent = String.format(
                    "log.%s(\"JDBC connecting with url:\" + %s + \" user:\" + %s + \" password:\" + %s);",
                    levelStr, url, user, password);

            PsiExpressionStatement logStmt =
                    (PsiExpressionStatement) factory.createStatementFromText(logContent, null);

            // 把日志语句插到原赋值语句的下一行
            assignmentExpression.getParent().getNextSibling().replace(logStmt);

        } catch (IncorrectOperationException e) {
            LOG.error(e);
        }
    }
}