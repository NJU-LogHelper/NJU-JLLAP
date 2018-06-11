package edu.nju.codeInspection;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import edu.nju.quickfixes.javalogging.configlogging.BranchJavaConfigQuickfix;
import edu.nju.quickfixes.javalogging.configlogging.SwitchJavaConfigQuickfix;
import edu.nju.quickfixes.javalogging.finelogging.BranchJavaFineQuickfix;
import edu.nju.quickfixes.javalogging.finelogging.SwitchJavaFineQuickfix;
import edu.nju.quickfixes.javalogging.finerllogging.BranchJavaFinerQuickfix;
import edu.nju.quickfixes.javalogging.finerllogging.SwitchJavaFinerQuickfix;
import edu.nju.quickfixes.javalogging.finestlogging.BranchJavaFinestQuickfix;
import edu.nju.quickfixes.javalogging.finestlogging.SwitchJavaFinestQuickfix;
import edu.nju.quickfixes.javalogging.infologging.BranchJavaInfoQuickfix;
import edu.nju.quickfixes.javalogging.infologging.SwitchJavaInfoQuickfix;
import edu.nju.quickfixes.javalogging.severelogging.BranchJavaSevereQuickfix;
import edu.nju.quickfixes.javalogging.severelogging.SwitchJavaSevereQuickfix;
import edu.nju.quickfixes.javalogging.warnlogging.BranchJavaWarningQuickfix;
import edu.nju.quickfixes.javalogging.warnlogging.SwitchJavaWarningQuickfix;
import edu.nju.quickfixes.log4jcommonslogging.fatallogging.BranchLog4jFatalQuickfix;
import edu.nju.quickfixes.log4jcommonslogging.fatallogging.SwitchLog4jFatalQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.debuglogging.BranchSlf4jDebugQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.debuglogging.SwitchSlf4jDebugQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.errorlogging.BranchSlf4jErrorQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.errorlogging.SwitchSlf4jErrorQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.infologging.BranchSlf4jInfoQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.infologging.SwitchSlf4jInfoQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.tracelogging.BranchSlf4jTraceQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.tracelogging.SwitchSlf4jTraceQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.warnlogging.BranchSlf4jWarnQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.warnlogging.SwitchSlf4jWarnQuickfix;
import edu.nju.util.LevelSequenceUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

public class SwitchInspection extends BaseJavaLocalInspectionTool {

    private final LocalQuickFix switchJavaConfigQuickfix = new SwitchJavaConfigQuickfix();
    private final LocalQuickFix switchJavaInfoQuickfix= new SwitchJavaInfoQuickfix();
    private final LocalQuickFix switchJavaFineQuickfix = new SwitchJavaFineQuickfix();
    private final LocalQuickFix switchJavaFinerQuickfix = new SwitchJavaFinerQuickfix();
    private final LocalQuickFix switchJavaFinestQuickfix = new SwitchJavaFinestQuickfix();
    private final LocalQuickFix switchJavaSevereQuickfix = new SwitchJavaSevereQuickfix();
    private final LocalQuickFix switchJavaWarningQuickfix = new SwitchJavaWarningQuickfix();
    private final LocalQuickFix switchLog4jFatalQuickfix = new SwitchLog4jFatalQuickfix();
    private final LocalQuickFix switchSlf4jDebugQuickfix = new SwitchSlf4jDebugQuickfix();
    private final LocalQuickFix switchSlf4jErrorQuickfix = new SwitchSlf4jErrorQuickfix();
    private final LocalQuickFix switchSlf4jInfoQuickfix = new SwitchSlf4jInfoQuickfix();
    private final LocalQuickFix switchSlf4jTraceQuickfix = new SwitchSlf4jTraceQuickfix();
    private final LocalQuickFix switchSlf4jWarnQuickfix = new SwitchSlf4jWarnQuickfix();



    @SuppressWarnings({"WeakerAccess"})
    @NonNls
    public String CHECKED_CLASSES = "java.lang.String;java.util.Date";

    @SuppressWarnings({"WeakAccess"})
    @NonNls
    private static final String DESCRIPTION_TEMPLATE = "Switch分支需要被记录";

    @NotNull
    public String getDisplayName() {
        return "重要switch需要记日志";
    }

    @NotNull
    public String getGroupDisplayName() {
        return GroupNames.BUGS_GROUP_NAME;
    }

    //对应html

    @NotNull
    public String getShortName() {
        return "Switch";
    }


    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {

            @Override
            public void visitReferenceExpression(PsiReferenceExpression psiReferenceExpression) {

            }


            /**
             *
             * @param
             */
            @Override
            public void visitSwitchStatement(PsiSwitchStatement psiSwitchStatement) {

                super.visitSwitchStatement(psiSwitchStatement);

                PsiExpression psiExpression = psiSwitchStatement.getExpression();
                PsiCodeBlock codeBlock=psiSwitchStatement.getBody();

                if(psiExpression==null || codeBlock==null){
                    return;
                }

                //如果switch语句中有log语句，则不打
                PsiStatement[] psiStatements = codeBlock.getStatements();
                if (psiStatements.length != 0) {
                    for (PsiStatement psiStatement : psiStatements) {

                        if (psiStatement instanceof PsiExpressionStatement) {
                            PsiExpression expression = ((PsiExpressionStatement) psiStatement).getExpression();
                            //判断是否是函数调用
                            if (expression instanceof PsiMethodCallExpression) {
                                //是否有log
                                if (Objects.requireNonNull(((PsiMethodCallExpression) expression).getMethodExpression().getQualifierExpression()).getText().equals("log")) {
                                    return;
                                }
                            }
                        }
                    }
                }

                //
                List<LocalQuickFix> quickFixes = LevelSequenceUtil.getQuickfixSequence("edu.nju.codeInspection.SwitchInspection","switch","switch");
                for (int i = 0;i<quickFixes.size();++i){
                    LocalQuickFix quickFix = quickFixes.get(i);
                    holder.registerProblem(psiExpression,DESCRIPTION_TEMPLATE,quickFix);
                }

            }
        };
    }
    public boolean isEnabledByDefault() {
        return true;
    }
}
