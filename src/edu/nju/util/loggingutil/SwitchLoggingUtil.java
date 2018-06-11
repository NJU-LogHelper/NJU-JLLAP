package edu.nju.util.loggingutil;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public class SwitchLoggingUtil {

    private static final Logger LOG = Logger.getInstance("#com.com.intellij.codeInspection.BranchStatementsInspection");

    public static void doSwitchLogging(@NotNull Project project, @NotNull ProblemDescriptor descriptor, LoggingType type, Object level, int typeId) {
        try {
            PsiSwitchStatement psiSwitchStatement = (PsiSwitchStatement) descriptor.getPsiElement().getParent();
            PsiCodeBlock codeBlock = psiSwitchStatement.getBody();
            PsiElement[] psiElements = codeBlock.getChildren();
            System.out.println(11111111);
            for (PsiElement psiElement : psiElements) {
                if (psiElement instanceof PsiSwitchLabelStatement) {

                    PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
                    PsiExpressionStatement logCall;

                    //获取switch各分支的value
                    PsiExpression psiExpression = ((PsiSwitchLabelStatement) psiElement).getCaseValue();

                    String levelString = LoggingUtil.getLevelStringById(level, typeId);
                    String stmt = null;

                    if (psiExpression == null) {
                        //进入default
                        stmt="enter case" + psiExpression.getText() + " please_input_your_branch_name ";
                    } else {
                        //进入case分支
                        stmt="enter default  please_input_your_branch_name ";
                    }
                    String logContent = String.format("%s %s %s\");","log.",levelString,stmt);
                    logCall = (PsiExpressionStatement) factory.createStatementFromText(logContent, null);

                    codeBlock.addAfter(logCall, psiElement);
                }
            }
        } catch (IncorrectOperationException e) {
            LOG.error(e);
        }

    }

}
