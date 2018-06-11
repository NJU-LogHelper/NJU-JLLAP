package edu.nju.codeInspection;


import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import edu.nju.quickfixes.javalogging.configlogging.ExceptionJavaConfigQuickfix;
import edu.nju.quickfixes.javalogging.finelogging.ExceptionJavaFineQuickfix;
import edu.nju.quickfixes.javalogging.finerllogging.ExceptionJavaFinerQuickfix;
import edu.nju.quickfixes.javalogging.finestlogging.ExceptionJavaFinestQuickfix;
import edu.nju.quickfixes.javalogging.infologging.ExceptionJavaInfoQuickfix;
import edu.nju.quickfixes.javalogging.severelogging.ExceptionJavaSevereQuickfix;
import edu.nju.quickfixes.javalogging.warnlogging.ExceptionJavaWarningQuickfix;
import edu.nju.quickfixes.log4jcommonslogging.fatallogging.ExceptionLog4jFatalQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.debuglogging.ExceptionSlf4jDebugQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.errorlogging.ExceptionSlf4jErrorQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.infologging.ExceptionSlf4jInfoQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.tracelogging.ExceptionSlf4jTraceQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.warnlogging.ExceptionSlf4jWarnQuickfix;
import edu.nju.util.LevelSequenceUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;


public class ExceptionInspection extends BaseJavaLocalInspectionTool {

    private final LocalQuickFix exceptionJavaConfigQuickfix = new ExceptionJavaConfigQuickfix();
    private final LocalQuickFix exceptionJavaInfoQuickfix= new ExceptionJavaInfoQuickfix();
    private final LocalQuickFix exceptionJavaFineQuickfix = new ExceptionJavaFineQuickfix();
    private final LocalQuickFix exceptionJavaFinerQuickfix = new ExceptionJavaFinerQuickfix();
    private final LocalQuickFix exceptionJavaFinestQuickfix = new ExceptionJavaFinestQuickfix();
    private final LocalQuickFix exceptionJavaSevereQuickfix = new ExceptionJavaSevereQuickfix();
    private final LocalQuickFix exceptionJavaWarningQuickfix = new ExceptionJavaWarningQuickfix();
    private final LocalQuickFix exceptionLog4jFatalQuickfix = new ExceptionLog4jFatalQuickfix();
    private final LocalQuickFix exceptionSlf4jDebugQuickfix = new ExceptionSlf4jDebugQuickfix();
    private final LocalQuickFix exceptionSlf4jErrorQuickfix = new ExceptionSlf4jErrorQuickfix();
    private final LocalQuickFix exceptionSlf4jInfoQuickfix = new ExceptionSlf4jInfoQuickfix();
    private final LocalQuickFix exceptionSlf4jTraceQuickfix = new ExceptionSlf4jTraceQuickfix();
    private final LocalQuickFix exceptionSlf4jWarnQuickfix = new ExceptionSlf4jWarnQuickfix();



    @SuppressWarnings({"WeakAccess"})
    @NonNls
    private static final String DESCRIPTION_TEMPLATE = "Catch到异常,需要输出log";

    @NotNull
    public String getDisplayName() {
        return "Catch到的异常";
    }

    @NotNull
    public String getGroupDisplayName() {
        return GroupNames.BUGS_GROUP_NAME;
    }

    @NotNull
    public String getShortName() {
        return "Exception";
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitReferenceExpression(PsiReferenceExpression psiReferenceExpression) {
            }


            @Override
            public void visitCatchSection(PsiCatchSection section) {
                super.visitCatchSection(section);
                PsiCodeBlock psiCodeBlock = section.getCatchBlock();
                if (psiCodeBlock == null) {
                    return;
                }
                PsiStatement[] psiStatements = psiCodeBlock.getStatements();
                if (psiStatements.length != 0) {
                    for (PsiStatement psiStatement : psiStatements) {
                        //判断是否有log语句
                        if (psiStatement instanceof PsiExpressionStatement) {
                            PsiExpression expression = ((PsiExpressionStatement) psiStatement).getExpression();
                            if (expression instanceof PsiMethodCallExpression) {
                                if (Objects.requireNonNull(((PsiMethodCallExpression) expression).getMethodExpression().getQualifierExpression()).getText().equals("log")) {
                                    return;
                                }
                            }
                        }
                    }
                }
                List<LocalQuickFix> quickFixes = LevelSequenceUtil.getQuickfixSequence("edu.nju.codeInspection.ExceptionInspection","catch","exception");
                for (int i = 0;i<quickFixes.size();++i){
                    LocalQuickFix quickFix = quickFixes.get(i);
                    holder.registerProblem(section,DESCRIPTION_TEMPLATE,quickFix);
                }

            }
        };
    }


    public boolean isEnabledByDefault() {
        return true;
    }
}
