package edu.nju.util.loggingutil;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.apache.commons.logging.Log;
import org.jetbrains.annotations.NotNull;

public class ExceptionLoggingUtil {
    private static final Logger LOG = Logger.getInstance("#edu.nju.codeInspection.ExceptionInspection");

    public static void doExceptionLogging(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor, LoggingType type, Object level, int typeId) {
        PsiCatchSection psiCatchSection = (PsiCatchSection) problemDescriptor.getPsiElement();

        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
        String exceptionTypeName = "";
        String exceptionName = "";
        if (psiCatchSection.getCatchType() != null && psiCatchSection.getParameter() != null) {
            exceptionTypeName = psiCatchSection.getCatchType().getCanonicalText();
            exceptionName = psiCatchSection.getParameter().getName();
        }

        String levelString = LoggingUtil.getLevelStringById(level, typeId);
        String stmt = exceptionTypeName + "\"+" + exceptionName;
        String logContent = String.format("%s %s %s\");","log.",levelString,stmt);

        PsiExpressionStatement logStatement= (PsiExpressionStatement) factory.createStatementFromText(logContent,null);
        PsiCodeBlock catchBlock = psiCatchSection.getCatchBlock();
        if(catchBlock==null){
            LOG.error("catchBlock is null");
        }else{
            logStatement= (PsiExpressionStatement) catchBlock.getFirstBodyElement().replace(logStatement);
//            LOG.info(new ResolveLoggerInstance().calculateResult(new Expression[0], (ExpressionContext) logStatement.getContext()).toString());
        }
    }
}
