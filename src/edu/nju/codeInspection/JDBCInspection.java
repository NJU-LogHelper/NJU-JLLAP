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
 * Created by chentiange on 2018/5/5.
 */

/**
 * JDBCInspection 是一个 IntelliJ 本地 Java 检查器，用于检测项目中未记录日志的 JDBC 操作。
 *
 * 主要检测点：
 * - 对 java.sql.DriverManager.getConnection(...) 的赋值（Connection 获取）是否被记录；
 * - 对 java.sql.Statement 的 executeQuery/execute/executeUpdate 调用（ResultSet/boolean/int 初始化）是否被记录；
 *
 * 当检测到未记录的 JDBC 操作时，该检查器会通过一组 LocalQuickFix 建议不同日志框架与级别的修复方案。
 * 注意：仅添加注释，不改变逻辑。
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
     * 返回在检查器设置/界面中显示的可读名称。
     */
    @NotNull
    public String getDisplayName() {

        return "JDBC connection should be logged";
    }

    /**
     * 检查器所属分组（用于在 IntelliJ 检查设置中分类）。
     */
    @NotNull
    public String getGroupDisplayName() {
        return GroupNames.LOGGING_GROUP_NAME;
    }

    //对应的html
    /**
     * 检查器的短名称（用于元数据和描述文件）。
     */
    @NotNull
    public String getShortName() {
        return "JDBCLogging";
    }

    /**
     * 构建 PSI visitor，用于遍历 Java AST 并发现未记录的 JDBC 操作。
     *
     * 覆盖关键方法：
     * - visitAssignmentExpression: 检测 DriverManager.getConnection 的赋值（Connection）
     * - visitDeclarationStatement: 检测 Statement.executeQuery/execute/executeUpdate 的结果未被记录
     *
     * 对于每个发现的问题，会调用 holder.registerProblem(...) 并附带若干 quickfix。
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
                // 如果右侧是 null 字面量，则跳过检测
                if(isNullLiteral(expression)){
                    return;
                }
                // 检测赋值操作符是否为 '='，并进一步检查是否为 DriverManager.getConnection 的调用并且未被记录
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
                // 遍历声明的变量，检查初始化表达式：
                // - ResultSet 类型且调用 executeQuery -> 标记为 "query" 需要记录
                // - boolean 类型且调用 execute -> 标记为 "execute" 需要记录
                // - int 类型且调用 executeUpdate -> 标记为 "update" 需要记录
                // 使用 LoggingUtil.isLogged(...) 跳过已经记录的语句
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

    /**
     * 判断表达式是否为 null 字面量。
     */
    private static boolean isNullLiteral(PsiExpression expr) {
        return expr instanceof PsiLiteralExpression && "null".equals(expr.getText());
    }


    /**
     * 是否默认启用该检查器（IDE 中的开关默认值）。
     */
    public boolean isEnabledByDefault() {
        return true;
    }
}
