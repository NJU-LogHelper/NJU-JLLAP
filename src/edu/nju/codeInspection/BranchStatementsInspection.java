package edu.nju.codeInspection;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import edu.nju.quickfixes.javalogging.configlogging.BranchJavaConfigQuickfix;
import edu.nju.quickfixes.javalogging.finelogging.BranchJavaFineQuickfix;
import edu.nju.quickfixes.javalogging.finerllogging.BranchJavaFinerQuickfix;
import edu.nju.quickfixes.javalogging.finestlogging.BranchJavaFinestQuickfix;
import edu.nju.quickfixes.javalogging.infologging.BranchJavaInfoQuickfix;
import edu.nju.quickfixes.javalogging.severelogging.BranchJavaSevereQuickfix;
import edu.nju.quickfixes.javalogging.warnlogging.BranchJavaWarningQuickfix;
import edu.nju.quickfixes.log4jcommonslogging.fatallogging.BranchLog4jFatalQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.debuglogging.BranchSlf4jDebugQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.errorlogging.BranchSlf4jErrorQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.infologging.BranchSlf4jInfoQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.tracelogging.BranchSlf4jTraceQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.warnlogging.BranchSlf4jWarnQuickfix;
import edu.nju.util.LevelSequenceUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;


/**
 * 检测分支语句
 * if if-else switch
 */
public class BranchStatementsInspection extends BaseJavaLocalInspectionTool {
    private final LocalQuickFix branchJavaConfigQuickfix = new BranchJavaConfigQuickfix();
    private final LocalQuickFix branchJavaInfoQuickfix = new BranchJavaInfoQuickfix();
    private final LocalQuickFix branchJavaFineQuickfix = new BranchJavaFineQuickfix();
    private final LocalQuickFix branchJavaFinerQuickfix = new BranchJavaFinerQuickfix();
    private final LocalQuickFix branchJavaFinestQuickfix = new BranchJavaFinestQuickfix();
    private final LocalQuickFix branchJavaSevereQuickfix = new BranchJavaSevereQuickfix();
    private final LocalQuickFix branchJavaWarningQuickfix = new BranchJavaWarningQuickfix();
    private final LocalQuickFix branchLog4jFatalQuickfix = new BranchLog4jFatalQuickfix();
    private final LocalQuickFix branchSlf4jDebugQuickfix = new BranchSlf4jDebugQuickfix();
    private final LocalQuickFix branchSlf4jErrorQuickfix = new BranchSlf4jErrorQuickfix();
    private final LocalQuickFix branchSlf4jInfoQuickfix = new BranchSlf4jInfoQuickfix();
    private final LocalQuickFix branchSlf4jTraceQuickfix = new BranchSlf4jTraceQuickfix();
    private final LocalQuickFix branchSlf4jWarnQuickfix = new BranchSlf4jWarnQuickfix();

    private static final String CLASS_NAME = "edu.nju.codeInspection.BranchStatementsInspection";
    private static final String TYPE = "if";
    private static final String OPE = "critical";

    @SuppressWarnings({"WeakerAccess"})
    @NonNls
    public String CHECKED_CLASSES = "java.lang.String;java.util.Date";
//    @NonNls
//    private static final String DESCRIPTION_TEMPLATE =
//            InspectionsBundle.message("inspection.branchStatements.problem.somethingwrong");

    @NotNull
    public String getDisplayName() {
        return " if/ if-else need to log";
    }

    @NotNull
    public String getGroupDisplayName() {
        return GroupNames.BUGS_GROUP_NAME;
    }
    //对应html

    @NotNull
    public String getShortName() {
        return "BranchLogging";
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaRecursiveElementVisitor() {

            private boolean isSynchronizedCheckHint = false;
            private boolean isNullCheckHint = false;
            private boolean isLengthCheckHint = false;
            private boolean isValidationHint = false;
            private boolean isZeroCheckHint = false;

            @Override
            public void visitIfStatement(PsiIfStatement statement) {

                super.visitIfStatement(statement);

                // 先获得if-else代码块  在里面写提示"if-else hhh"
                final PsiExpression condition = statement.getCondition();

                //获取then分支
                PsiBlockStatement thenBranch = (PsiBlockStatement) statement.getThenBranch();

                //获取else分支
                PsiBlockStatement elseBranch = (PsiBlockStatement) statement.getElseBranch();

                if (thenBranch != null && !InspectionUtils.isNotLogged(thenBranch.getCodeBlock().getStatements())) {
                    return;
                }

                if (elseBranch != null && !InspectionUtils.isNotLogged(elseBranch.getCodeBlock().getStatements())) {
                    return;
                }

                if (condition != null) {
                    condition.acceptChildren(new JavaRecursiveElementVisitor() {

                        @Override
                        public void visitJavaToken(PsiJavaToken token) {
                            super.visitJavaToken(token);
                            if (isNullCheckHint) {
                                return;
                            }
                            if (token.getText().equals("==")) {
                                condition.acceptChildren(new JavaElementVisitor() {
                                    @Override
                                    public void visitLiteralExpression(PsiLiteralExpression expression) {
                                        super.visitLiteralExpression(expression);
                                        if (expression.getText().equals("null")) {
                                            holder.registerProblem(condition, "Null check is recommended to be logged.", getLocalQuickFixes());
                                            isNullCheckHint = true;
                                        }
                                    }
                                });
                            } else if (token.getText().equals("!=")) {
                                condition.acceptChildren(new JavaElementVisitor() {
                                    @Override
                                    public void visitLiteralExpression(PsiLiteralExpression expression) {
                                        super.visitLiteralExpression(expression);
                                        if (expression.getText().equals("null")) {
                                            holder.registerProblem(condition, "Null check is recommended to be logged.", getLocalQuickFixes());
                                            isNullCheckHint = true;
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void visitReferenceExpression(PsiReferenceExpression expression) {
                            super.visitReferenceExpression(expression);
                            String referenceName = expression.getReferenceName();
                            if (referenceName != null) {
                                referenceName = referenceName.toLowerCase();
                                if (referenceName.equals("length")) {
                                    if (isLengthCheckHint) {
                                        return;
                                    }
                                    holder.registerProblem(condition, "Length check is recommended to be logged.", getLocalQuickFixes());
                                    isLengthCheckHint = true;
                                } else if (referenceName.contains("valid") || referenceName.contains("validate") || referenceName.contains("validation")) {
                                    if (isValidationHint) {
                                        return;
                                    }
                                    holder.registerProblem(condition, "Validation is recommended to be logged.", getLocalQuickFixes());
                                    isValidationHint = true;
                                }
                            }
                        }

                        @Override
                        public void visitLiteralExpression(PsiLiteralExpression expression) {
                            super.visitLiteralExpression(expression);
                            if (expression.textContains('0')) {
                                if (isZeroCheckHint) {
                                    return;
                                }
                                holder.registerProblem(condition, "Zero check is recommended to be logged.", getLocalQuickFixes());
                                isZeroCheckHint = true;
                            }
                        }
                    });
                }
            }


            @Override
            public void visitMethod(PsiMethod method) {
                super.visitMethod(method);
                method.accept(new JavaRecursiveElementVisitor() {
                    @Override
                    public void visitKeyword(PsiKeyword keyword) {
                        super.visitKeyword(keyword);
                        if (keyword.getText().equals(PsiKeyword.SYNCHRONIZED)) {
                            method.accept(new JavaRecursiveElementVisitor() {
                                @Override
                                public void visitIfStatement(PsiIfStatement statement) {
                                    super.visitIfStatement(statement);
                                    if (isSynchronizedCheckHint) {
                                        return;
                                    }
                                    PsiBlockStatement thenBranch = (PsiBlockStatement) statement.getThenBranch();
                                    PsiBlockStatement elseBranch = (PsiBlockStatement) statement.getElseBranch();
                                    if ((thenBranch != null && !InspectionUtils.isNotLogged(thenBranch.getCodeBlock().getStatements()))
                                            || (elseBranch != null && !InspectionUtils.isNotLogged(elseBranch.getCodeBlock().getStatements()))) {
                                        return;
                                    }
                                    if (statement.getCondition() != null) {
                                        holder.registerProblem(statement.getCondition(), "Branch in synchronized method is recommended to be logged.", getLocalQuickFixes());
                                        isSynchronizedCheckHint = true;
                                    }
                                }
                            });
                        }
                    }
                });
            }
        };
    }


    @Override
    public JComponent createOptionsPanel() {
        return null;
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

    private LocalQuickFix[] getLocalQuickFixes() {
        List<LocalQuickFix> list = LevelSequenceUtil.getQuickfixSequence(CLASS_NAME, TYPE, OPE);
        return list.toArray(new LocalQuickFix[list.size()]);
    }

}
