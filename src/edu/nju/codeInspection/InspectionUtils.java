package edu.nju.codeInspection;

import com.intellij.psi.*;

public class InspectionUtils {

    /**
     * Check whether the statements is logged
     */
    public static boolean isNotLogged(PsiStatement[] psiStatements) {
        if (psiStatements == null || psiStatements.length == 0) {
            return true;
        }
        for (PsiStatement statement : psiStatements) {
            if (statement instanceof PsiExpressionStatement) {
                PsiExpression expression = ((PsiExpressionStatement) statement).getExpression();
                if (expression instanceof PsiMethodCallExpression) {
                    PsiReferenceExpression referenceExpression = ((PsiMethodCallExpression) expression).getMethodExpression();
                    if (referenceExpression.getQualifierExpression() != null) {
                        if (referenceExpression.getQualifierExpression().getText().equals("log")) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
