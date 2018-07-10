package edu.nju.codeInspection;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * assert无quickfix
 */
public class AssertInspection extends BaseJavaLocalInspectionTool {
    @SuppressWarnings({"WeakAccess"})
    @NonNls
    private static final String DESCRIPTION_TEMPLATE = "assert后需要输出描述信息";

    @NotNull
    public String getDisplayName() {
        return "assert判断";
    }

    @NotNull
    public String getGroupDisplayName() {
        return GroupNames.BUGS_GROUP_NAME;
    }

    @NotNull
    public String getShortName() {
        return "AssertLogging";
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitReferenceExpression(PsiReferenceExpression psiReferenceExpression) {
            }

            @Override
            public void visitAssertStatement(PsiAssertStatement statement) {
                super.visitAssertStatement(statement);
                if (statement.getAssertDescription() == null) {
                    holder.registerProblem(statement, DESCRIPTION_TEMPLATE);
                }
            }

        };
    }
    public boolean isEnabledByDefault() {
        return true;
    }
}
