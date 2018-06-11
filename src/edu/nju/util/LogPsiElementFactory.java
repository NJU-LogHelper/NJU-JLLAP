package edu.nju.util;

import com.intellij.psi.*;

public interface LogPsiElementFactory
{
    PsiField createField(final String p0, final PsiType p1, final PsiElement p2);
    
    PsiType createTypeFromText(final String p0, final PsiElement p1);
    
    PsiElement createExpressionFromText(final String p0, final PsiElement p1);
    
    PsiElement createStatementFromText(final String p0, final PsiElement p1);
    
    PsiElement createWhiteSpaceFromText(final String p0);
}
