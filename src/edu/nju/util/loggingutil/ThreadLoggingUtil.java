package edu.nju.util.loggingutil;


import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

/**
 * Created by chentiange on 2018/5/5.
 */
public class ThreadLoggingUtil {
    private static final Logger LOG = Logger.getInstance("#com.intellij.codeInspection.inspections.ThreadInspection");
    public static void doThreadLogging(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor, LoggingType type, Object level, int typeId){
        try {
            PsiMethodCallExpression methodExpression = (PsiMethodCallExpression) problemDescriptor.getPsiElement();
            final PsiElement semicolon = methodExpression.getNextSibling();
//            final CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(project);

            PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
            //get thread type
            final PsiMethod method = methodExpression.resolveMethod();
            final String methodName = method.getName();
            final PsiExpressionList argumentList = methodExpression.getArgumentList();
            String arguments = "without arguments\"";
            if (argumentList != null){
                PsiExpression[] expressions = argumentList.getExpressions();
                if (expressions.length > 0){
                    arguments = " with argument: ";
                    for (PsiExpression expression:expressions){
                        arguments = arguments + "\"+"+expression.getText()+"+\", ";
                    }
                    arguments = arguments.substring(0,arguments.length()-4);
                }
            }

            final PsiClass containingClass = method.getContainingClass();
            final String className = containingClass.getQualifiedName();
            final PsiClass currentFileClass = PsiTreeUtil.getParentOfType(methodExpression, PsiClass.class);
            final String currentFileClassName = currentFileClass.getName();

            //get thread id
//            PsiMethodCallExpression getIdCall =
//                    (PsiMethodCallExpression) factory.createExpressionFromText("Thread.currentThread().getId()", null);

            String logContent = String.format("%s%s Thread: \"+\"%s id: \"+Thread.currentThread().getId()+\" %s %s );","log.", LoggingUtil.getLevelStringById(level,typeId), className,methodName,arguments);
//            PsiMethodCallExpression logCall =
//                    (PsiMethodCallExpression) factory.createExpressionFromText(logContent, null);
            PsiExpressionStatement logstmt = (PsiExpressionStatement) factory.createStatementFromText(logContent,null);
            methodExpression.getParent().getNextSibling().replace(logstmt);
//            methodExpression.addAfter(logstmt,methodExpression.getParent());

//            logCall.addAfter(semicolon,logCall);

//            PsiWhiteSpaceImpl whiteSpace = new PsiWhiteSpaceImpl("\n");
//            methodExpression.addAfter(whiteSpace,methodExpression.getParent());



//            methodExpression.addAfter(logCall,methodExpression.getParent());
//            logCall.addBefore(whiteSpace,logCall);




//            codeStyleManager.reformat(currentFileClass);




        } catch (IncorrectOperationException e) {
            LOG.error(e);
        }

    }
}
