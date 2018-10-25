package edu.nju.util.loggingutil;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public class BranchStatementLoggingUtil {
    private static final Logger LOG = Logger.getInstance("#com.com.intellij.codeInspection.BranchStatementsInspection");

    public static void doBranchLogging(@NotNull Project project, @NotNull ProblemDescriptor descriptor, LoggingType type, Object level, int typeId) {
        try {

            //获取分支主元素
            PsiIfStatement ifStatement = (PsiIfStatement) descriptor.getPsiElement().getParent();

            //暂时支持单层的if-else
            PsiBlockStatement ifBlock = (PsiBlockStatement) ifStatement.getThenBranch();
            PsiBlockStatement elseBlock = (PsiBlockStatement) ifStatement.getElseBranch();

            LogPsiBlockStatement(ifBlock, project, level, typeId);
            LogPsiBlockStatement(elseBlock, project, level, typeId);

        } catch (IncorrectOperationException e) {
            LOG.error(e);
        }
    }


    private static void LogPsiBlockStatement(PsiBlockStatement psiBlockStatement, Project project, Object level, int typeId) {
        if (psiBlockStatement == null) {
            return;
        }
        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
        String levelString = LoggingUtil.getLevelStringById(level, typeId);
        String stmt = "your_log_msg";
        String logContent = String.format("%s %s%s\");", "log.", levelString, stmt);

        PsiExpressionStatement logCall = (PsiExpressionStatement) factory.createStatementFromText(logContent, null);

        PsiCodeBlock psiCodeBlock = psiBlockStatement.getCodeBlock();
        PsiStatement[] psiStatements = psiCodeBlock.getStatements();
        if (psiStatements.length != 0) {
            psiCodeBlock.addBefore(logCall, psiStatements[0]);
        }
        psiCodeBlock.add(logCall);
    }

}
