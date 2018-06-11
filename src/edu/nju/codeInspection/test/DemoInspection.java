package edu.nju.codeInspection.test;

import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiDoWhileStatement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class DemoInspection extends BaseJavaLocalInspectionTool {


    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
//        return super.buildVisitor(holder, isOnTheFly);
        return new JavaElementVisitor() {

            @Override
            public void visitDoWhileStatement(PsiDoWhileStatement statement) {
                super.visitDoWhileStatement(statement);
            }
        };
    }
}
