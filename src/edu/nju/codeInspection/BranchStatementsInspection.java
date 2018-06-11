package edu.nju.codeInspection;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.formatting.WhiteSpace;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.StdLanguages;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.tree.IElementType;
import com.intellij.ui.DocumentAdapter;
import com.intellij.util.IncorrectOperationException;
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
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * 检测分支语句
 * if if-else switch
 */
public class BranchStatementsInspection extends BaseJavaLocalInspectionTool {
    private final LocalQuickFix branchJavaConfigQuickfix = new BranchJavaConfigQuickfix();
    private final LocalQuickFix branchJavaInfoQuickfix= new BranchJavaInfoQuickfix();
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
        return "BranchStatements";
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {

            /**
             *
             * @param
             */
            @Override
            public void visitIfStatement(PsiIfStatement statement) {

                super.visitIfStatement(statement);

                // 先获得if-else代码块  在里面写提示"if-else hhh"
                PsiExpression expression = statement.getCondition();

                //获取then分支
                PsiBlockStatement thenbranch = (PsiBlockStatement) statement.getThenBranch();

                //获取else分支
                PsiBlockStatement elsebranch = (PsiBlockStatement) statement.getElseBranch();
                if(expression==null|| thenbranch ==null || elsebranch==null){
                    return;
                }
                PsiStatement[] thenbranchs = thenbranch.getCodeBlock().getStatements();

                PsiStatement[] elsebranchs = elsebranch.getCodeBlock().getStatements();

                if (findRepeatStatement(thenbranchs, "log") == false || findRepeatStatement(elsebranchs, "log") == false) {
                    //开始报问题
                    List<LocalQuickFix> quickFixes = LevelSequenceUtil.getQuickfixSequence("edu.nju.codeInspection.BranchStatementsInspection","if","critical");
                    for (int i = 0;i<quickFixes.size();++i){
                        LocalQuickFix quickFix = quickFixes.get(i);
                        holder.registerProblem(expression,"重要分支语句缺少log",quickFix);
                    }

                }
            }

            //查重复
            private boolean findRepeatStatement(PsiStatement[] psiStatements,String judgeString){
                if (psiStatements.length != 0) {
                    for (PsiStatement psiStatement : psiStatements) {
                        //判断代码块中是否有log语句
                        if (psiStatement instanceof PsiExpressionStatement) {
                            PsiExpression expr = ((PsiExpressionStatement) psiStatement).getExpression();
                            if (expr instanceof PsiMethodCallExpression) {
                                if (Objects.requireNonNull(((PsiMethodCallExpression) expr).getMethodExpression().getQualifierExpression()).getText().equals(judgeString)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                return false;
            }

        };
    }


    public JComponent createOptionsPanel() {
        return null;
    }

    public boolean isEnabledByDefault() {
        return true;
    }
}
