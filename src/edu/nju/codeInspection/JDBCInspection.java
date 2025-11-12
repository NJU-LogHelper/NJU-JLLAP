package edu.nju.codeInspection;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import edu.nju.quickfixes.javalogging.configlogging.JDBCConnectionJavaConfigQuickfix;
import edu.nju.quickfixes.javalogging.configlogging.JDBCExecuteJavaConfigQuickfix;
import edu.nju.quickfixes.javalogging.configlogging.JDBCQueryJavaConfigQuickfix;
import edu.nju.quickfixes.javalogging.configlogging.JDBCUpdateJavaConfigQuickfix;
import edu.nju.quickfixes.javalogging.finelogging.JDBCConnectionJavaFineQuickfix;
import edu.nju.quickfixes.javalogging.finelogging.JDBCExecuteJavaFineQuickfix;
import edu.nju.quickfixes.javalogging.finelogging.JDBCQueryJavaFineQuickfix;
import edu.nju.quickfixes.javalogging.finelogging.JDBCUpdateJavaFineQuickfix;
import edu.nju.quickfixes.javalogging.finerllogging.JDBCConnectionJavaFinerQuickfix;
import edu.nju.quickfixes.javalogging.finerllogging.JDBCExecuteJavaFinerQuickfix;
import edu.nju.quickfixes.javalogging.finerllogging.JDBCQueryJavaFinerQuickfix;
import edu.nju.quickfixes.javalogging.finerllogging.JDBCUpdateJavaFinerQuickfix;
import edu.nju.quickfixes.javalogging.finestlogging.JDBCConnectionJavaFinestQuickfix;
import edu.nju.quickfixes.javalogging.finestlogging.JDBCExecuteJavaFinestQuickfix;
import edu.nju.quickfixes.javalogging.finestlogging.JDBCQueryJavaFinestQuickfix;
import edu.nju.quickfixes.javalogging.finestlogging.JDBCUpdateJavaFinestQuickfix;
import edu.nju.quickfixes.javalogging.infologging.JDBCConnectionJavaInfoQuickfix;
import edu.nju.quickfixes.javalogging.infologging.JDBCExecuteJavaInfoQuickfix;
import edu.nju.quickfixes.javalogging.infologging.JDBCQueryJavaInfoQuickfix;
import edu.nju.quickfixes.javalogging.infologging.JDBCUpdateJavaInfoQuickfix;
import edu.nju.quickfixes.javalogging.severelogging.JDBCConnectionJavaSevereQuickfix;
import edu.nju.quickfixes.javalogging.severelogging.JDBCExecuteJavaSevereQuickfix;
import edu.nju.quickfixes.javalogging.severelogging.JDBCQueryJavaSevereQuickfix;
import edu.nju.quickfixes.javalogging.severelogging.JDBCUpdateJavaSevereQuickfix;
import edu.nju.quickfixes.javalogging.warnlogging.JDBCConnectionJavaWarningQuickfix;
import edu.nju.quickfixes.javalogging.warnlogging.JDBCExecuteJavaWarnQuickfix;
import edu.nju.quickfixes.javalogging.warnlogging.JDBCQueryJavaWarnQuickfix;
import edu.nju.quickfixes.javalogging.warnlogging.JDBCUpdateJavaWarnQuickfix;
import edu.nju.quickfixes.log4jcommonslogging.fatallogging.JDBCConnectionLog4jFatalQuickfix;
import edu.nju.quickfixes.log4jcommonslogging.fatallogging.JDBCExecuteLog4jFatalQuickfix;
import edu.nju.quickfixes.log4jcommonslogging.fatallogging.JDBCQueryLog4jFatalQuickfix;
import edu.nju.quickfixes.log4jcommonslogging.fatallogging.JDBCUpdateLog4jFatalQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.debuglogging.JDBCConnectionSlf4jDebugQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.debuglogging.JDBCExecuteSlf4jDebugQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.debuglogging.JDBCQuerySlf4jDebugQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.debuglogging.JDBCUpdateSlf4jDebugQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.errorlogging.JDBCConnectionSlf4jErrorQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.errorlogging.JDBCExecuteSlf4jErrorQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.errorlogging.JDBCQuerySlf4jErrorQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.errorlogging.JDBCUpdateSlf4jErrorQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.infologging.JDBCConnectionSlf4jInfoQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.infologging.JDBCExecuteSlf4jInfoQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.infologging.JDBCQuerySlf4jInfoQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.infologging.JDBCUpdateSlf4jInfoQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.tracelogging.JDBCConnectionSlf4jTraceQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.tracelogging.JDBCExecuteSlf4jTraceQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.tracelogging.JDBCQuerySlf4jTraceQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.tracelogging.JDBCUpdateSlf4jTraceQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.warnlogging.JDBCConnectionSlf4jWarnQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.warnlogging.JDBCExecuteSlf4jWarnQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.warnlogging.JDBCQuerySlf4jWarnQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.warnlogging.JDBCUpdateSlf4jWarnQuickfix;
import edu.nju.util.LevelSequenceUtil;
import edu.nju.util.loggingutil.LoggingUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * JDBCInspection 是一个 IntelliJ IDEA 的本地 Java 检查器（LocalInspectionTool）。
 * 目的：检测未记录日志的 JDBC 操作并提供一组 quickfix 建议（不同日志框架与级别）。
 *
 * 主要检测点：
 *  - 对 DriverManager.getConnection(...) 的赋值（Connection 获取）是否未记录日志；
 *  - 对 Statement.executeQuery/execute/executeUpdate 的调用（ResultSet/boolean/int 初始化）是否未记录日志。
 *
 * 检测到的问题会使用 LevelSequenceUtil 提供的一系列 LocalQuickFix 进行修复建议，
 * 并通过 LoggingUtil.isLogged(...) 跳过已记录的语句以避免重复报告。
 */
public class JDBCInspection extends BaseJavaLocalInspectionTool {

    private final LocalQuickFix jdbcConnectionJavaInfoQuickfix = new JDBCConnectionJavaInfoQuickfix();
    private final LocalQuickFix jdbcConnectionJavaConfigQuickfix = new JDBCConnectionJavaConfigQuickfix();
    private final LocalQuickFix jdbcConnectionJavaFineQuickfix = new JDBCConnectionJavaFineQuickfix();
    private final LocalQuickFix jdbcConnectionJavaFinerQuickfix = new JDBCConnectionJavaFinerQuickfix();
    private final LocalQuickFix jdbcConnectionJavaFinestQuickfix = new JDBCConnectionJavaFinestQuickfix();
    private final LocalQuickFix jdbcConnectionJavaSevereQuickfix = new JDBCConnectionJavaSevereQuickfix();
    private final LocalQuickFix jdbcConnectionJavaWarningQuickfix = new JDBCConnectionJavaWarningQuickfix();
    private final LocalQuickFix jdbcConnectionLog4jFatalQuickfix = new JDBCConnectionLog4jFatalQuickfix();
    private final LocalQuickFix jdbcConnectionSlf4jDebugQuickfix = new JDBCConnectionSlf4jDebugQuickfix();
    private final LocalQuickFix jdbcConnectionSlf4jErrorQuickfix = new JDBCConnectionSlf4jErrorQuickfix();
    private final LocalQuickFix jdbcConnectionSlf4jInfoQuickfix = new JDBCConnectionSlf4jInfoQuickfix();
    private final LocalQuickFix jdbcConnectionSlf4jTraceQuickfix = new JDBCConnectionSlf4jTraceQuickfix();
    private final LocalQuickFix jdbcConnectionSlf4jWarnQuickfix = new JDBCConnectionSlf4jWarnQuickfix();


    private final LocalQuickFix jdbcQueryJavaInfoQuickfix = new JDBCQueryJavaInfoQuickfix();
    private final LocalQuickFix jdbcQueryJavaConfigQuickfix= new JDBCQueryJavaConfigQuickfix();
    private final LocalQuickFix jdbcQueryJavaFineQuickfix = new JDBCQueryJavaFineQuickfix();
    private final LocalQuickFix jdbcQueryJavaFinerQuickfix = new JDBCQueryJavaFinerQuickfix();
    private final LocalQuickFix jdbcQueryJavaFinestQuickfix = new JDBCQueryJavaFinestQuickfix();
    private final LocalQuickFix jdbcQueryJavaSevereQuickfix = new JDBCQueryJavaSevereQuickfix();
    private final LocalQuickFix jdbcQueryJavaWarnQuickfix = new JDBCQueryJavaWarnQuickfix();
    private final LocalQuickFix jdbcQueryLog4jFatalQuickfix = new JDBCQueryLog4jFatalQuickfix();
    private final LocalQuickFix jdbcQuerySlf4jDebugQuickfix = new JDBCQuerySlf4jDebugQuickfix();
    private final LocalQuickFix jdbcQuerySlf4jErrorQuickfix = new JDBCQuerySlf4jErrorQuickfix();
    private final LocalQuickFix jdbcQuerySlf4jInfoQuickfix = new JDBCQuerySlf4jInfoQuickfix();
    private final LocalQuickFix jdbcQuerySlf4jTraceQuickfix = new JDBCQuerySlf4jTraceQuickfix();
    private final LocalQuickFix jdbcQuerySlf4jWarnQuickfix = new JDBCQuerySlf4jWarnQuickfix();



    private final LocalQuickFix jdbcExecuteJavaInfoQuickfix = new JDBCExecuteJavaInfoQuickfix();
    private final LocalQuickFix jdbcExecuteJavaConfigQuickfix = new JDBCExecuteJavaConfigQuickfix();
    private final LocalQuickFix jdbcExecuteJavaFineQuickfix = new JDBCExecuteJavaFineQuickfix();
    private final LocalQuickFix jdbcExecuteJavaFinerQuickfix = new JDBCExecuteJavaFinerQuickfix();
    private final LocalQuickFix jdbcExecuteJavaFinestQuickfix = new JDBCExecuteJavaFinestQuickfix();
    private final LocalQuickFix jdbcExecuteJavaSevereQuickfix = new JDBCExecuteJavaSevereQuickfix();
    private final LocalQuickFix jdbcExecuteJavaWarnQuickfix = new JDBCExecuteJavaWarnQuickfix();
    private final LocalQuickFix jdbcExecuteLog4jFatalQuickfix = new JDBCExecuteLog4jFatalQuickfix();
    private final LocalQuickFix jdbcExecuteSlf4jDebugQuickfix = new JDBCExecuteSlf4jDebugQuickfix();
    private final LocalQuickFix jdbcExecuteSlf4jErrorQuickfix = new JDBCExecuteSlf4jErrorQuickfix();
    private final LocalQuickFix jdbcExecuteSlf4jInfoQuickfix = new JDBCExecuteSlf4jInfoQuickfix();
    private final LocalQuickFix jdbcExecuteSlf4jTraceQuickfix = new JDBCExecuteSlf4jTraceQuickfix();
    private final LocalQuickFix jdbcExecuteSlf4jWarnQuickfix = new JDBCExecuteSlf4jWarnQuickfix();


    private final LocalQuickFix jdbcUpdateJavaInfoQuickfix = new JDBCUpdateJavaInfoQuickfix();
    private final LocalQuickFix jdbcUpdateJavaConfigQuickfix = new JDBCUpdateJavaConfigQuickfix();
    private final LocalQuickFix jdbcUpdateJavaFineQuickfix = new JDBCUpdateJavaFineQuickfix();
    private final LocalQuickFix jdbcUpdateJavaFinerQuickfix = new JDBCUpdateJavaFinerQuickfix();
    private final LocalQuickFix jdbcUpdateJavaFinestQuickfix = new JDBCUpdateJavaFinestQuickfix();
    private final LocalQuickFix jdbcUpdateJavaSevereQuickfix = new JDBCUpdateJavaSevereQuickfix();
    private final LocalQuickFix jdbcUpdateJavaWarnQuickfix = new JDBCUpdateJavaWarnQuickfix();
    private final LocalQuickFix jdbcUpdateLog4jFatalQuickfix = new JDBCUpdateLog4jFatalQuickfix();
    private final LocalQuickFix jdbcUpdateSlf4jDebugQuickfix = new JDBCUpdateSlf4jDebugQuickfix();
    private final LocalQuickFix jdbcUpdateSlf4jErrorQuickfix = new JDBCUpdateSlf4jErrorQuickfix();
    private final LocalQuickFix jdbcUpdateSlf4jInfoQuickfix = new JDBCUpdateSlf4jInfoQuickfix();
    private final LocalQuickFix jdbcUpdateSlf4jTraceQuickfix = new JDBCUpdateSlf4jTraceQuickfix();
    private final LocalQuickFix jdbcUpdateSlf4jWarnQuickfix = new JDBCUpdateSlf4jWarnQuickfix();


    private static final String DESCRIPTION_TEMPLATE = "inspection.JDBC.descriptor";

    /**
     * 返回该检查器在设置或 UI 中显示的名称。
     */
    @NotNull
    public String getDisplayName() {

        return "JDBC connection should be logged";
    }

    /**
     * 返回该检查器所属的分组显示名称（这里使用 IntelliJ 的日志分组）。
     */
    @NotNull
    public String getGroupDisplayName() {
        return GroupNames.LOGGING_GROUP_NAME;
    }

    //对应的html
    /**
     * 检查器的短名称，用于配置标识与 HTML 描述中使用。
     */
    @NotNull
    public String getShortName() {
        return "JDBCLogging";
    }

    /**
     * 构建用于遍历 PSI 树的 visitor。
     *
     * 覆盖的主要方法：
     *  - visitAssignmentExpression: 检查 Connection 赋值（DriverManager.getConnection）
     *  - visitDeclarationStatement: 检查 Statement.executeQuery / execute / executeUpdate 的使用
     *
     * 在匹配到未记录的 JDBC 操作时，使用 holder.registerProblem 注册问题并附带多个 quickfix 建议。
     */
    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {

            @Override
            public void visitReferenceExpression(PsiReferenceExpression psiReferenceExpression) {
            }

            //1. check connection
            @Override
            public void visitAssignmentExpression(PsiAssignmentExpression expression) {
                super.visitAssignmentExpression(expression);
                if(isNullLiteral(expression)){
                    return;
                }
                final IElementType opSign = expression.getOperationTokenType();
                if (opSign == JavaTokenType.EQ){
                    final PsiExpression lConn = expression.getLExpression();
                    if (expression.getRExpression().textContains('.')) {
                        final PsiMethodCallExpression rGetConn = (PsiMethodCallExpression) expression.getRExpression();

                        //check left
                        if (lConn != null && lConn.getType().toString().equals("PsiType:Connection")) {
                            final PsiReferenceExpression methodExpression = rGetConn.getMethodExpression();
                            final String methodName = methodExpression.getReferenceName();
                            //check right
                            //check method name
                            if (methodName != null && methodName.equals("getConnection")) {
                                //check call class
                                final PsiMethod method = rGetConn.resolveMethod();
                                final PsiClass containingClass = method.getContainingClass();
                                final String className = containingClass.getQualifiedName();
                                if (className != null && className.equals("java.sql.DriverManager")&& !LoggingUtil.isLogged((PsiStatement) expression.getParent())) {
                                    List<LocalQuickFix> quickFixes = LevelSequenceUtil.getQuickfixSequence("edu.nju.codeInspection.JDBCInspection","methodcall","connection");
                                    for (int i = 0;i<quickFixes.size();++i){
                                        LocalQuickFix quickFix = quickFixes.get(i);
                                        holder.registerProblem(expression,"jdbc connection should be logged",quickFix);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //2. check execute
            @Override
            public void visitDeclarationStatement(PsiDeclarationStatement statement) {
                super.visitDeclarationStatement(statement);
                if (statement == null){
                    return;
                }
                for (PsiElement declaredElement:statement.getDeclaredElements()){
                    if (declaredElement instanceof PsiVariable){
                        PsiVariable variable = (PsiVariable) declaredElement;
                        PsiExpression initializer = variable.getInitializer();
                        if (initializer != null) {
                            //1. execute query


                            /**
                             * 判断条件前后改变解决了bug
                             */
                            if (initializer instanceof PsiMethodCallExpression&&initializer.getType().toString().equals("PsiType:ResultSet")) {
                                final PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) initializer;
                                final PsiReferenceExpression queryMethodExpression = methodCallExpression.getMethodExpression();
                                final String queryMethodName = queryMethodExpression.getReferenceName();
                                if (queryMethodName != null && queryMethodName.equals("executeQuery")) {
                                    final PsiMethod queryMethod = methodCallExpression.resolveMethod();
                                    final PsiClass queryClass = queryMethod.getContainingClass();
                                    final String fullClassName = queryClass.getQualifiedName();
                                    if (fullClassName != null && fullClassName.equals("java.sql.Statement") &&!LoggingUtil.isLogged(statement)) {
//                                        List<LocalQuickFix> quickFixes = LevelSequenceUtil.getQuickfixSequence("edu.nju.codeInspection.JDBCInspection","methodcall","query");
//                                        for (int i = 0;i<quickFixes.size();++i){
//                                            LocalQuickFix quickFix = quickFixes.get(i);
                                            holder.registerProblem(statement,"jdbc query should be logged "+LevelSequenceUtil.getPercentStr("methodcall"),jdbcQueryJavaConfigQuickfix,jdbcQueryJavaFineQuickfix,jdbcQueryJavaFinerQuickfix,jdbcQueryJavaFinestQuickfix,jdbcQueryJavaInfoQuickfix
                                            ,jdbcQueryJavaSevereQuickfix,jdbcQueryJavaWarnQuickfix,jdbcQueryLog4jFatalQuickfix,jdbcQuerySlf4jDebugQuickfix,jdbcQuerySlf4jErrorQuickfix,jdbcQuerySlf4jInfoQuickfix,jdbcQuerySlf4jTraceQuickfix,jdbcQuerySlf4jWarnQuickfix);
//                                        }
                                    }
                                }
                            }else if (initializer instanceof PsiMethodCallExpression&&initializer.getType().toString().equals("PsiType:boolean") ) {
                                final PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) initializer;
                                final PsiReferenceExpression queryMethodExpression = methodCallExpression.getMethodExpression();
                                final String queryMethodName = queryMethodExpression.getReferenceName();
                                if (queryMethodName != null && queryMethodName.equals("execute")) {
                                    final PsiMethod queryMethod = methodCallExpression.resolveMethod();
                                    final PsiClass queryClass = queryMethod.getContainingClass();
                                    final String fullClassName = queryClass.getQualifiedName();
                                    if (fullClassName != null && fullClassName.equals("java.sql.Statement") &&!LoggingUtil.isLogged(statement)) {
//                                        List<LocalQuickFix> quickFixes = LevelSequenceUtil.getQuickfixSequence("edu.nju.codeInspection.JDBCInspection","methodcall","execute");
//                                        for (int i = 0;i<quickFixes.size();++i){
//                                            LocalQuickFix quickFix = quickFixes.get(i);
                                            holder.registerProblem(statement,"jdbc execute should be logged "+LevelSequenceUtil.getPercentStr("methodcall"),jdbcExecuteJavaConfigQuickfix,jdbcExecuteJavaFineQuickfix,jdbcExecuteJavaFinerQuickfix,jdbcExecuteJavaFinestQuickfix,jdbcExecuteJavaInfoQuickfix,jdbcExecuteJavaSevereQuickfix,
                                                    jdbcExecuteJavaWarnQuickfix,jdbcExecuteLog4jFatalQuickfix,jdbcExecuteSlf4jDebugQuickfix,jdbcExecuteSlf4jErrorQuickfix,jdbcExecuteSlf4jInfoQuickfix,jdbcExecuteSlf4jTraceQuickfix,jdbcExecuteSlf4jWarnQuickfix);
//                                        }
                                    }
                                }
                            } else if (initializer instanceof PsiMethodCallExpression&&initializer.getType().toString().equals("PsiType:int") ) {
                                final PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) initializer;
                                final PsiReferenceExpression queryMethodExpression = methodCallExpression.getMethodExpression();
                                final String queryMethodName = queryMethodExpression.getReferenceName();
                                if (queryMethodName != null && queryMethodName.equals("executeUpdate")) {
                                    final PsiMethod queryMethod = methodCallExpression.resolveMethod();
                                    final PsiClass queryClass = queryMethod.getContainingClass();
                                    final String fullClassName = queryClass.getQualifiedName();
                                    if (fullClassName != null && fullClassName.equals("java.sql.Statement") &&!LoggingUtil.isLogged(statement)) {
//                                        List<LocalQuickFix> quickFixes = LevelSequenceUtil.getQuickfixSequence("edu.nju.codeInspection.JDBCInspection","methodcall","update");
//                                        for (int i = 0;i<quickFixes.size();++i){
//                                            LocalQuickFix quickFix = quickFixes.get(i);
                                            holder.registerProblem(statement,"jdbc update should be logged "+LevelSequenceUtil.getPercentStr("methodcall"),jdbcUpdateJavaConfigQuickfix,jdbcUpdateJavaFineQuickfix,jdbcUpdateJavaFinerQuickfix,jdbcUpdateJavaFinestQuickfix,jdbcUpdateJavaInfoQuickfix,
                                                    jdbcUpdateJavaSevereQuickfix,jdbcUpdateJavaWarnQuickfix,jdbcUpdateLog4jFatalQuickfix,jdbcUpdateSlf4jDebugQuickfix,jdbcUpdateSlf4jErrorQuickfix,jdbcUpdateSlf4jInfoQuickfix,
                                                    jdbcUpdateSlf4jTraceQuickfix,jdbcUpdateSlf4jWarnQuickfix);
//                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    private static boolean isNullLiteral(PsiExpression expr) {
        return expr instanceof PsiLiteralExpression && "null".equals(expr.getText());
    }


    public boolean isEnabledByDefault() {
        return true;
    }
}
