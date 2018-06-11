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
 */
public class JDBCConnectionLoggingUtil {
    private static final Logger LOG = Logger.getInstance("#com.intellij.codeInspection.inspections.JDBCInspection");
    public static void doJDBCLogging(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor, LoggingType type, Object level, int typeId){
        try {
            PsiAssignmentExpression assignmentExpression = (PsiAssignmentExpression) problemDescriptor.getPsiElement();
            final PsiMethodCallExpression rGetConn = (PsiMethodCallExpression) assignmentExpression.getRExpression();
            final PsiExpressionList expressionList = rGetConn.getArgumentList();
            final PsiExpression[] expressions = expressionList.getExpressions();
            String url = expressions[0].getText();
            String user = expressions[1].getText();
            String password = expressions[2].getText();

//            final PsiElement semicolon = assignmentExpression.getNextSibling();

            PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
            //get current class
            final PsiClass currentFileClass = PsiTreeUtil.getParentOfType(assignmentExpression, PsiClass.class);
            final String currentFileClassName = currentFileClass.getName();
            String levelString = LoggingUtil.getLevelStringById(level,typeId);
            String logContent = String.format("%s%s  JDBC connecting with url:\"+ %s +\" user: \"+ %s+\" password: \"+%s);","log.", levelString,url,user,password);
//            PsiMethodCallExpression logCall =
//                    (PsiMethodCallExpression) factory.createExpressionFromText(logContent, null);
//            logCall.addAfter(semicolon,logCall);
//            assignmentExpression.addAfter(logCall,assignmentExpression.getParent());
            PsiExpressionStatement logstmt = (PsiExpressionStatement) factory.createStatementFromText(logContent,null);
            assignmentExpression.getParent().getNextSibling().replace(logstmt);
//            (logstmt,assignmentExpressionon.getParent());


        } catch (IncorrectOperationException e) {
            LOG.error(e);
        }

    }
}
