package edu.nju.util.loggingutil;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

/**
 * Created by chentiange on 2018/5/12.
 */
public class JDBCExecuteLoggingUtil {
    private static final Logger LOG = Logger.getInstance("#com.intellij.codeInspection.inspections.JDBCInspection");
    public static void doJDBCLogging(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor, LoggingType type, Object level, int typeId){
        try {
            PsiDeclarationStatement declarationStatement = (PsiDeclarationStatement) problemDescriptor.getPsiElement();

            PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
            //get current class
            final PsiClass currentFileClass = PsiTreeUtil.getParentOfType(declarationStatement, PsiClass.class);
            final String currentFileClassName = currentFileClass.getName();
            PsiElement[] elements = declarationStatement.getDeclaredElements();
            if (elements.length!= 0){
                PsiVariable variable = (PsiVariable) elements[0];
                PsiExpression initializer = variable.getInitializer();
                if (initializer != null) {
                    final PsiMethodCallExpression queryExpression = (PsiMethodCallExpression) initializer;
                    final PsiExpressionList argumentList = queryExpression.getArgumentList();
                    final PsiReferenceExpression methodExpression = queryExpression.getMethodExpression();
                    final String methodName = methodExpression.getReferenceName();
                    String sql = "null";
                    if (argumentList != null){
                        PsiExpression[] expressions = argumentList.getExpressions();
                        if (expressions.length > 0){
                            sql = "";
                            for (PsiExpression expression:expressions){
                                sql = sql + "\"+"+expression.getText()+"+\", ";
                            }
                            sql = sql.substring(0,sql.length()-4);
                        }
                    }


                    String logContent = String.format("%s%s  JDBC %s: %s);","log.",LoggingUtil.getLevelStringById(level,typeId),methodName,sql);
//                    PsiMethodCallExpression logCall =
//                            (PsiMethodCallExpression) factory.createExpressionFromText(logContent, null);
//
//                    PsiElement semicolon = queryExpression.getNextSibling();
//                    logCall.addAfter(semicolon,logCall);
//                    declarationStatement.addAfter(logCall,declarationStatement);
                    PsiExpressionStatement logstmt = (PsiExpressionStatement) factory.createStatementFromText(logContent,null);
                    declarationStatement.getNextSibling().replace(logstmt);
//                    declarationStatement.addAfter(logstmt,declarationStatement);
                }
            }

        } catch (IncorrectOperationException e) {
            LOG.error(e);
        }

    }
}
