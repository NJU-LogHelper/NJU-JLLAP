package edu.nju.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileVisitor;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.psi.util.PsiUtilCore;
import edu.nju.util.loggingutil.LoggingUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 代码分析Action
 * 分析Java代码中各种关键位置的日志记录情况，检查是否存在缺失的日志记录
 * 支持的检查规则包括：断言、异常、关键分支、线程、文件、服务器、数据库、关键类和方法
 * 
 * @author NJU
 */
public class AnalyzeAction extends AnAction {

    /** 当前项目 */
    private Project project;

    /** 日志记录器 */
    private final Logger logger = Logger.getInstance(this.getClass());

    /** 关键方法配置文件路径 */
    private final String criticalMethodsFile = this.getClass().getResource("/criticalMethod").getFile();
    /** 关键类配置文件路径 */
    private final String criticalClassesFile = this.getClass().getResource("/criticalClass").getFile();

    /** 规则类型：断言日志 */
    private static final int RULE_ASSERT = 0;
    /** 规则类型：异常日志 */
    private static final int RULE_EXCEPTION = 1;
    /** 规则类型：关键分支日志 */
    private static final int RULE_CRITICAL_BRANCH = 2;
    /** 规则类型：线程日志 */
    private static final int RULE_THREAD = 3;
    /** 规则类型：文件日志 */
    private static final int RULE_FILE = 4;
    /** 规则类型：服务器日志 */
    private static final int RULE_SERVER = 5;
    /** 规则类型：数据库日志 */
    private static final int RULE_DATABASE = 6;
    /** 规则类型：关键类和方法日志 */
    private static final int RULE_CRITICAL_CLASSES_AND_METHODS = 7;

    /** 规则名称数组 */
    private static final String[] NAMES = {
            "Assert Log",
            "Exception Log",
            "Critical Branch Log",
            "Thread Log",
            "File Log",
            "Server Log",
            "Database Log",
            "Critical Classes and Methods Log"
    };

    /** 各规则类型的总计数数组 */
    private static final int[] TOTAL_COUNT = {0, 0, 0, 0, 0, 0, 0, 0};

    /** 各规则类型的缺失计数数组 */
    private static final int[] MISS_COUNT = {0, 0, 0, 0, 0, 0, 0, 0};

    /**
     * 执行Action逻辑
     * 遍历项目中的所有Java文件，分析日志记录情况，并显示分析结果
     * 
     * @param e Action事件
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        this.project = e.getProject();

        Objects.requireNonNull(project);
        // 获取项目的所有源代码根目录
        VirtualFile[] vFiles = ProjectRootManager.getInstance(project).getContentSourceRoots();
        StringBuilder builder = new StringBuilder();
        // 递归遍历所有Java文件并进行分析
        for (VirtualFile vFile : vFiles) {
            VfsUtilCore.visitChildrenRecursively(vFile, new VirtualFileVisitor<Object>() {
                @Override
                public boolean visitFile(@NotNull VirtualFile file) {
                    if (!file.isDirectory() && file.getExtension() != null && file.getExtension().equals("java")) {
                        analyze(PsiManager.getInstance(project).findFile(file));
                    }
                    return super.visitFile(file);
                }
            });
        }

//        builder.append(NAMES[RULE_ASSERT]).append(": needs ").append(TOTAL_COUNT[RULE_ASSERT]).append(", but misses ").append(MISS_COUNT[RULE_ASSERT]).append(".\n");
//        builder.append(NAMES[RULE_EXCEPTION]).append(": needs ").append(TOTAL_COUNT[RULE_EXCEPTION]).append(", but misses ").append(MISS_COUNT[RULE_EXCEPTION]).append(".\n");
        builder.append(NAMES[RULE_CRITICAL_BRANCH]).append(": needs ").append(TOTAL_COUNT[RULE_CRITICAL_BRANCH]).append(", but misses ").append(MISS_COUNT[RULE_CRITICAL_BRANCH]).append(".\n");
//        builder.append(NAMES[RULE_THREAD]).append(": needs ").append(TOTAL_COUNT[RULE_THREAD]).append(", but misses ").append(MISS_COUNT[RULE_THREAD]).append(".\n");
//        builder.append(NAMES[RULE_FILE]).append(": needs ").append(TOTAL_COUNT[RULE_FILE]).append(", but misses ").append(MISS_COUNT[RULE_FILE]).append(".\n");
//        builder.append(NAMES[RULE_SERVER]).append(": needs ").append(TOTAL_COUNT[RULE_SERVER]).append(", but misses ").append(MISS_COUNT[RULE_SERVER]).append(".\n");
//        builder.append(NAMES[RULE_DATABASE]).append(": needs ").append(TOTAL_COUNT[RULE_DATABASE]).append(", but misses ").append(MISS_COUNT[RULE_DATABASE]).append(".\n");
//        builder.append(NAMES[RULE_CRITICAL_CLASSES_AND_METHODS]).append(": needs ").append(TOTAL_COUNT[RULE_CRITICAL_CLASSES_AND_METHODS]).append(", but misses ").append(MISS_COUNT[RULE_CRITICAL_CLASSES_AND_METHODS]).append(".");
        Messages.showInfoMessage(builder.toString(), "Log Analysis Result");
    }

    /**
     * 分析PSI文件
     * 使用访问者模式遍历AST，检查各种关键位置的日志记录情况
     * 
     * @param psiFile 要分析的PSI文件
     */
    private void analyze(PsiFile psiFile) {
        psiFile.accept(new JavaRecursiveElementWalkingVisitor() {

            /**
             * 访问类定义
             * 检查关键类中的方法是否需要记录日志
             */
            @Override
            public void visitClass(PsiClass aClass) {
                super.visitClass(aClass);
                // 只处理顶级类，忽略内部类
                if (!(aClass.getParent() instanceof PsiJavaFile)) {
                    return;
                }

                String line = "";
                try {
                    // 读取关键类配置，检查当前类是否匹配
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(criticalClassesFile));
                    line = bufferedReader.readLine();
                    while (line != null) {
                        String currentClassWholeName = LoggingUtil.getCurrentClassWholeName(aClass);
                        if (Pattern.matches(line, currentClassWholeName)) {
                            //get all method
                            PsiMethod[] methods = aClass.getMethods();
                            // 检查类中所有非main方法是否需要记录日志
                            for (PsiMethod method : methods) {
                                if (!method.getName().equals("main")) {
                                    TOTAL_COUNT[RULE_CRITICAL_CLASSES_AND_METHODS]++;
                                    if (isToLog(method)) {
                                        MISS_COUNT[RULE_CRITICAL_CLASSES_AND_METHODS]++;
                                    }
                                }
                            }
                        }
                        line = bufferedReader.readLine();
                    }
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            /**
             * 访问方法定义
             * 检查关键方法是否需要记录日志
             */
            @Override
            public void visitMethod(PsiMethod method) {
                super.visitMethod(method);
                String line = "";
                final PsiClass currentFileClass = PsiTreeUtil.getParentOfType(method, PsiClass.class);
                try {
                    // 读取关键方法配置，检查当前方法是否匹配
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(criticalMethodsFile));
                    line = bufferedReader.readLine();
                    while (line != null) {
                        final String currentClassWholeName = LoggingUtil.getCurrentClassWholeName(currentFileClass);
                        final String currentMethodWholeName = currentClassWholeName + ":" + method.getName();
                        if (Pattern.matches(line, currentMethodWholeName) && isToLog(method)) {
                            TOTAL_COUNT[RULE_CRITICAL_CLASSES_AND_METHODS]++;
                            if (isToLog(method)) {
                                MISS_COUNT[RULE_CRITICAL_CLASSES_AND_METHODS]++;
                            }
                        }
                        line = bufferedReader.readLine();
                    }
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            /**
             * 访问断言语句
             * 检查断言是否有描述信息
             */
            @Override
            public void visitAssertStatement(PsiAssertStatement statement) {
                super.visitAssertStatement(statement);
                TOTAL_COUNT[RULE_ASSERT]++;
                // 如果断言没有描述信息，则计入缺失计数
                if (statement.getAssertDescription() == null) {
                    MISS_COUNT[RULE_ASSERT]++;
                }
            }

            /**
             * 访问if语句
             * 检查if-else分支中是否都有日志记录
             */
            @Override
            public void visitIfStatement(PsiIfStatement statement) {
                super.visitIfStatement(statement);

                // 先获得if-else代码块  在里面写提示"if-else hhh"
                PsiExpression expression = statement.getCondition();

                //获取then分支
                PsiStatement thenbranch = statement.getThenBranch();

                //获取else分支
                PsiStatement elsebranch = statement.getElseBranch();
                // 只处理完整的if-else语句
                if (expression == null || thenbranch == null || elsebranch == null) {
                    return;
                }

                TOTAL_COUNT[RULE_CRITICAL_BRANCH]++;
                boolean isThenRepeated, isElseRepeated = false;
                // 检查then分支是否有日志记录
                if (thenbranch instanceof PsiBlockStatement) {
                    isThenRepeated = findRepeatStatement(((PsiBlockStatement) thenbranch).getCodeBlock().getStatements());
                } else {
                    isThenRepeated = findRepeatStatement(thenbranch);
                }
                // 检查else分支是否有日志记录
                if (elsebranch instanceof PsiBlockStatement) {
                    isElseRepeated = findRepeatStatement(((PsiBlockStatement) elsebranch).getCodeBlock().getStatements());
                } else {
                    isElseRepeated = findRepeatStatement(elsebranch);
                }

                // 如果then或else分支缺少日志记录，计入缺失计数
                if (!isThenRepeated || !isElseRepeated) {
                    MISS_COUNT[RULE_CRITICAL_BRANCH]++;
                }
            }

            /**
             * 访问switch语句
             * 检查switch语句中是否有日志记录
             */
            @Override
            public void visitSwitchStatement(PsiSwitchStatement psiSwitchStatement) {

                super.visitSwitchStatement(psiSwitchStatement);

                PsiExpression psiExpression = psiSwitchStatement.getExpression();
                PsiCodeBlock codeBlock = psiSwitchStatement.getBody();

                if (psiExpression == null || codeBlock == null) {
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
                                TOTAL_COUNT[RULE_CRITICAL_BRANCH]++;
                                //是否有log
                                if (((PsiMethodCallExpression) expression).getMethodExpression().getQualifierExpression() != null
                                        && ((PsiMethodCallExpression) expression).getMethodExpression().getQualifierExpression().getText().equals("log")) {
                                    return;
                                }
                                MISS_COUNT[RULE_CRITICAL_BRANCH]++;
                            }
                        }
                    }
                }

            }

            /**
             * 访问catch块
             * 检查异常捕获块中是否有日志记录
             */
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
                                TOTAL_COUNT[RULE_EXCEPTION]++;
                                // 如果找到log语句，则不计入缺失
                                if (((PsiMethodCallExpression) expression).getMethodExpression().getQualifierExpression() != null
                                        && ((PsiMethodCallExpression) expression).getMethodExpression().getQualifierExpression().getText().equals("log")) {
                                    return;
                                }
                                MISS_COUNT[RULE_EXCEPTION]++;
                            }
                        }
                    }
                }

            }

            /**
             * 访问方法调用表达式
             * 检查线程相关操作（start、join、run）是否有日志记录
             */
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                super.visitCallExpression(expression);
                if (isNullLiteral(expression)) {
                    return;
                }
                final PsiReferenceExpression methodExpression = expression.getMethodExpression();
                final String methodName = methodExpression.getReferenceName();
                //check method name
                // 检查Thread的start和join方法
                if (methodName != null && (methodName.equals("start") || methodName.equals("join"))) {
                    //check call class type
                    if (isThread(expression)) {
                        TOTAL_COUNT[RULE_THREAD]++;
                        //check whether need register
                        if (!LoggingUtil.isLogged((PsiStatement) expression.getParent())) {
                            MISS_COUNT[RULE_THREAD]++;
                        }
                    }
                } else if (methodName != null && methodName.equals("run")) {
                    // 检查Thread子类的run方法
                    final PsiMethod method = expression.resolveMethod();
                    if (method == null) {
                        return;
                    }

                    final PsiClass psiClass = method.getContainingClass();
                    final PsiClass father;
                    if (psiClass != null) {
                        father = psiClass.getSuperClass();
                        if (father != null && father.getName().equals("Thread")) {
                            TOTAL_COUNT[RULE_THREAD]++;
                            if (!LoggingUtil.isLogged((PsiStatement) expression.getParent())) {
                                MISS_COUNT[RULE_THREAD]++;
                            }
                        }
                    }

                }
            }

            /**
             * 访问赋值表达式
             * 检查数据库连接获取操作（DriverManager.getConnection）是否有日志记录
             */
            @Override
            public void visitAssignmentExpression(PsiAssignmentExpression expression) {
                super.visitAssignmentExpression(expression);
                if (isNullLiteral(expression)) {
                    return;
                }
                final IElementType opSign = expression.getOperationTokenType();
                if (opSign == JavaTokenType.EQ) {
                    final PsiExpression lConn = expression.getLExpression();
                    if (expression.getRExpression().textContains('.')) {
                        if (expression.getRExpression() instanceof PsiMethodCallExpression) {
                            final PsiMethodCallExpression rGetConn = (PsiMethodCallExpression) expression.getRExpression();

                            //check left
                            // 检查是否为Connection类型的赋值
                            if (lConn != null && lConn.getType().toString().equals("PsiType:Connection")) {
                                final PsiReferenceExpression methodExpression = rGetConn.getMethodExpression();
                                final String methodName = methodExpression.getReferenceName();
                                //check right
                                //check method name
                                // 检查是否为DriverManager.getConnection调用
                                if (methodName != null && methodName.equals("getConnection")) {
                                    //check call class
                                    final PsiMethod method = rGetConn.resolveMethod();
                                    final PsiClass containingClass;
                                    if (method != null) {
                                        containingClass = method.getContainingClass();
                                        final String className;
                                        if (containingClass != null) {
                                            className = containingClass.getQualifiedName();
                                            if (className != null && className.equals("java.sql.DriverManager")) {
                                                TOTAL_COUNT[RULE_DATABASE]++;
                                                if (!LoggingUtil.isLogged((PsiStatement) expression.getParent())) {
                                                    MISS_COUNT[RULE_DATABASE]++;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            /**
             * 访问声明语句
             * 检查数据库操作（executeQuery、execute、executeUpdate）是否有日志记录
             */
            @Override
            public void visitDeclarationStatement(PsiDeclarationStatement statement) {
                super.visitDeclarationStatement(statement);
                if (statement == null) {
                    return;
                }
                for (PsiElement declaredElement : statement.getDeclaredElements()) {
                    if (declaredElement instanceof PsiVariable) {
                        PsiVariable variable = (PsiVariable) declaredElement;
                        PsiExpression initializer = variable.getInitializer();
                        if (initializer != null) {
                            //1. execute query


                            /**
                             * 判断条件前后改变解决了bug
                             * 检查executeQuery调用（返回ResultSet）
                             */
                            if (initializer instanceof PsiMethodCallExpression && initializer.getType() != null && initializer.getType().toString().equals("PsiType:ResultSet")) {
                                final PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) initializer;
                                final PsiReferenceExpression queryMethodExpression = methodCallExpression.getMethodExpression();
                                final String queryMethodName = queryMethodExpression.getReferenceName();
                                if (queryMethodName != null && queryMethodName.equals("executeQuery")) {
                                    final PsiMethod queryMethod = methodCallExpression.resolveMethod();
                                    final PsiClass queryClass = queryMethod.getContainingClass();
                                    final String fullClassName = queryClass.getQualifiedName();
                                    if (fullClassName != null && fullClassName.equals("java.sql.Statement")) {
                                        TOTAL_COUNT[RULE_DATABASE]++;
                                        if (!LoggingUtil.isLogged(statement)) {
                                            MISS_COUNT[RULE_DATABASE]++;
                                        }
                                    }
                                }
                            } 
                            // 检查execute调用（返回boolean）
                            else if (initializer instanceof PsiMethodCallExpression && initializer.getType() != null && initializer.getType().toString().equals("PsiType:boolean")) {
                                final PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) initializer;
                                final PsiReferenceExpression queryMethodExpression = methodCallExpression.getMethodExpression();
                                final String queryMethodName = queryMethodExpression.getReferenceName();
                                if (queryMethodName != null && queryMethodName.equals("execute")) {
                                    final PsiMethod queryMethod = methodCallExpression.resolveMethod();
                                    final PsiClass queryClass = queryMethod.getContainingClass();
                                    final String fullClassName = queryClass.getQualifiedName();
                                    if (fullClassName != null && fullClassName.equals("java.sql.Statement")) {
                                        TOTAL_COUNT[RULE_DATABASE]++;
                                        if (!LoggingUtil.isLogged(statement)) {
                                            MISS_COUNT[RULE_DATABASE]++;
                                        }
                                    }
                                }
                            } 
                            // 检查executeUpdate调用（返回int）
                            else if (initializer instanceof PsiMethodCallExpression && initializer.getType() != null && initializer.getType().toString().equals("PsiType:int")) {
                                final PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) initializer;
                                final PsiReferenceExpression queryMethodExpression = methodCallExpression.getMethodExpression();
                                final String queryMethodName = queryMethodExpression.getReferenceName();
                                if (queryMethodName != null && queryMethodName.equals("executeUpdate")) {
                                    final PsiMethod queryMethod = methodCallExpression.resolveMethod();
                                    final PsiClass queryClass = queryMethod.getContainingClass();
                                    final String fullClassName = queryClass.getQualifiedName();
                                    if (fullClassName != null && fullClassName.equals("java.sql.Statement")) {
                                        TOTAL_COUNT[RULE_DATABASE]++;
                                        if (!LoggingUtil.isLogged(statement)) {
                                            MISS_COUNT[RULE_DATABASE]++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            /**
             * 查找重复语句（检查是否有日志记录）
             * 在给定的语句数组中查找是否存在log语句
             * 
             * @param psiStatements 要检查的语句数组
             * @return 如果找到log语句返回true，否则返回false
             */
            private boolean findRepeatStatement(PsiStatement... psiStatements) {
                if (psiStatements.length != 0) {
                    for (PsiStatement psiStatement : psiStatements) {
                        //判断代码块中是否有log语句
                        if (psiStatement instanceof PsiExpressionStatement) {
                            PsiExpression expr = ((PsiExpressionStatement) psiStatement).getExpression();
                            if (expr instanceof PsiMethodCallExpression) {
                                if (((PsiMethodCallExpression) expr).getMethodExpression().getQualifierExpression() != null && ((PsiMethodCallExpression) expr).getMethodExpression().getQualifierExpression().getText().equals("log")) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                return false;
            }

            /**
             * 判断表达式是否为null字面量
             * 
             * @param expr 要检查的表达式
             * @return 如果是null字面量返回true，否则返回false
             */
            private boolean isNullLiteral(PsiExpression expr) {
                return expr instanceof PsiLiteralExpression && "null".equals(expr.getText());
            }

            /**
             * 判断方法调用表达式是否为Thread类的方法
             * 
             * @param expression 要检查的方法调用表达式
             * @return 如果是Thread类的方法返回true，否则返回false
             */
            private boolean isThread(PsiMethodCallExpression expression) {
                final PsiMethod method = expression.resolveMethod();
                boolean res = false;
                if (method == null) {
                    return res;
                }
                final PsiClass psiClass = method.getContainingClass();
                final String className = psiClass.getQualifiedName();
                if (className != null && className.equals("java.lang.Thread")) {
                    res = true;
                }
                return res;
            }

            /**
             * 判断方法是否需要记录日志
             * 检查方法开头和所有return语句前是否有日志记录
             * 
             * @param method 要检查的方法
             * @return 如果需要记录日志返回true，否则返回false
             */
            private boolean isToLog(PsiMethod method) {
                final PsiCodeBlock codeBlock = method.getBody();
                final PsiStatement[] statements = codeBlock.getStatements();
                if (statements.length == 0) return false;
                //check first - 检查方法开头是否有日志
                if (!statements[0].getText().startsWith("log.")) {
                    return true;
                }
                //check returns - 检查所有return语句前是否有日志
                boolean returnAllLogged = true;
                PsiType returnType = method.getReturnType();
                ;
                // void方法不需要检查return语句
                if (returnType == PsiType.VOID) {
                    return false;
                }

                PsiReturnStatement[] returnStatements = PsiUtil.findReturnStatements(method);
                for (PsiReturnStatement returnStatement : returnStatements) {
                    //check previous - 检查return语句前是否有日志
                    if (!returnStatement.getPrevSibling().getPrevSibling().getText().startsWith("log.")) {
                        returnAllLogged = false;
                    }
                }
                if (returnAllLogged) {
                    return false;
                }
                return true;
            }

        });
    }

}
