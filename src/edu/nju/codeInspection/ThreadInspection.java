package edu.nju.codeInspection;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import edu.nju.quickfixes.javalogging.configlogging.ThreadJavaConfigQuickfix;
import edu.nju.quickfixes.javalogging.finelogging.ThreadJavaFineQuickfix;
import edu.nju.quickfixes.javalogging.finerllogging.ThreadJavaFinerQuickfix;
import edu.nju.quickfixes.javalogging.infologging.ThreadJavaInfoQuickfix;
import edu.nju.quickfixes.javalogging.finestlogging.ThreadJavaFinestQuickfix;
import edu.nju.quickfixes.javalogging.severelogging.ThreadJavaSevereQuickfix;
import edu.nju.quickfixes.javalogging.warnlogging.ThreadJavaWarningQuickfix;
import edu.nju.quickfixes.log4jcommonslogging.fatallogging.ThreadLog4jFatalQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.debuglogging.ThreadSlf4jDebugQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.errorlogging.ThreadSlf4jErrorQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.infologging.ThreadSlf4jInfoQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.tracelogging.ThreadSlf4jTraceQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.warnlogging.ThreadSlf4jWarnQuickfix;
import com.intellij.psi.*;
import edu.nju.util.LevelSequenceUtil;
import edu.nju.util.loggingutil.LoggingUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * Created by chentiange on 2018/4/24.
 */
public class ThreadInspection extends BaseJavaLocalInspectionTool {


    private final LocalQuickFix threadJavaInfoQuickfix = new ThreadJavaInfoQuickfix();
    private final LocalQuickFix threadJavaFinestQuickfix = new ThreadJavaFinestQuickfix();
    private final LocalQuickFix threadJavaFinerQuickfix = new ThreadJavaFinerQuickfix();
    private final LocalQuickFix threadJavaFineQuickfix = new ThreadJavaFineQuickfix();
    private final LocalQuickFix threadJavaConfigQuickfix = new ThreadJavaConfigQuickfix();
    private final LocalQuickFix threadJavaWarningQuickfix = new ThreadJavaWarningQuickfix();
    private final LocalQuickFix threadJavaSevereQuickfix = new ThreadJavaSevereQuickfix();
    private final LocalQuickFix threadLog4jFatalQuickfix = new ThreadLog4jFatalQuickfix();
    private final LocalQuickFix threadSlf4jDebugQuickfix = new ThreadSlf4jDebugQuickfix();
    private final LocalQuickFix threadSlf4jErrorQuickfix = new ThreadSlf4jErrorQuickfix();
    private final LocalQuickFix threadSlf4jInfoQuickfix = new ThreadSlf4jInfoQuickfix();
    private final LocalQuickFix threadSlf4jTraceQuickfix = new ThreadSlf4jTraceQuickfix();
    private final LocalQuickFix threadSlf4jWarnQuickfix = new ThreadSlf4jWarnQuickfix();



    @SuppressWarnings({"WeakerAccess"})


    private static final String DESCRIPTION_TEMPLATE = "inspection.thread.descriptor";

    @NotNull
    public String getDisplayName() {

        return "Thread operation should be logged";
    }

    @NotNull
    public String getGroupDisplayName() {
        return GroupNames.LOGGING_GROUP_NAME;
    }

    //对应的html
    @NotNull
    public String getShortName() {
        return "ThreadLogging";
    }



    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {

            @Override
            public void visitReferenceExpression(PsiReferenceExpression psiReferenceExpression) {
            }


            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                super.visitCallExpression(expression);
                if(isNullLiteral(expression)){
                    return;
                }
                final PsiReferenceExpression methodExpression = expression.getMethodExpression();
                final String methodName = methodExpression.getReferenceName();
                //check method name
                if (methodName != null && (methodName.equals("start")||methodName.equals("join"))){
                    //check call class type
                    if (isThread(expression)){
                        //check whether need register
                        if(!LoggingUtil.isLogged((PsiStatement) expression.getParent())){
                            List<LocalQuickFix> quickFixes = LevelSequenceUtil.getQuickfixSequence("edu.nju.codeInspection.ThreadInspection","methodcall","thread");
                            for (int i = 0;i<quickFixes.size();++i){
                                LocalQuickFix quickFix = quickFixes.get(i);
                                holder.registerProblem(expression,"thread operation should be logged",quickFix);
                            }
                        }


                    }
                }else if (methodName != null && methodName.equals("run")){
                    final PsiMethod method = expression.resolveMethod();
                    if (method == null){
                        return ;
                    }
                    final PsiClass psiClass = method.getContainingClass();
                    final PsiClass father = psiClass.getSuperClass();
                    if (father.getName().equals("Thread") && !LoggingUtil.isLogged((PsiStatement) expression.getParent())){
//                        List<LocalQuickFix> quickFixes = LevelSequenceUtil.getQuickfixSequence("edu.nju.codeInspection.ThreadInspection","methodcall","thread");
//                        for (int i = 0;i<quickFixes.size();++i){
//                            LocalQuickFix quickFix = quickFixes.get(i);
                            holder.registerProblem(expression, "thread operation should be logged "+LevelSequenceUtil.getPercentStr("methodcall"), threadJavaConfigQuickfix,threadJavaFineQuickfix,threadJavaFinerQuickfix,threadJavaFinestQuickfix,
                                    threadJavaInfoQuickfix,threadJavaSevereQuickfix,threadJavaWarningQuickfix,threadLog4jFatalQuickfix,threadSlf4jDebugQuickfix,threadSlf4jErrorQuickfix,threadSlf4jInfoQuickfix,
                                    threadSlf4jTraceQuickfix,threadSlf4jWarnQuickfix);
//                        }
                    }
                }
            }
        };
    }

    private static boolean isNullLiteral(PsiExpression expr) {
        return expr instanceof PsiLiteralExpression && "null".equals(expr.getText());
    }

    private static boolean isThread(PsiMethodCallExpression expression){
        final PsiMethod method = expression.resolveMethod();
        boolean res = false;
        if (method == null){
            return res;
        }
        final PsiClass psiClass = method.getContainingClass();
        final String className = psiClass.getQualifiedName();
        if (className != null && className.equals("java.lang.Thread")){
            res = true;
        }
        return res;
    }


    public boolean isEnabledByDefault() {
        return true;
    }



}
