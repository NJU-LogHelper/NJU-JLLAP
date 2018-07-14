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

public class AnalyzeAction extends AnAction {

    private Project project;

    private final Logger logger = Logger.getInstance(this.getClass());

    private final String criticalMethodsFile = this.getClass().getResource("/criticalMethod").getFile();
    private final String criticalClassesFile = this.getClass().getResource("/criticalClass").getFile();

    private static final int RULE_ASSERT = 0;
    private static final int RULE_EXCEPTION = 1;
    private static final int RULE_CRITICAL_BRANCH = 2;
    private static final int RULE_THREAD = 3;
    private static final int RULE_FILE = 4;
    private static final int RULE_SERVER = 5;
    private static final int RULE_DATABASE = 6;
    private static final int RULE_CRITICAL_CLASSES_AND_METHODS = 7;

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

    private static final int[] TOTAL_COUNT = {0, 0, 0, 0, 0, 0, 0, 0};

    private static final int[] MISS_COUNT = {0, 0, 0, 0, 0, 0, 0, 0};

    @Override
    public void actionPerformed(AnActionEvent e) {
        this.project = e.getProject();

        Objects.requireNonNull(project);
        VirtualFile[] vFiles = ProjectRootManager.getInstance(project).getContentSourceRoots();
        StringBuilder builder = new StringBuilder();
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

    private void analyze(PsiFile psiFile) {
        psiFile.accept(new JavaRecursiveElementWalkingVisitor() {

            @Override
            public void visitClass(PsiClass aClass) {
                super.visitClass(aClass);
                if (!(aClass.getParent() instanceof PsiJavaFile)) {
                    return;
                }

                String line = "";
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(criticalClassesFile));
                    line = bufferedReader.readLine();
                    while (line != null) {
                        String currentClassWholeName = LoggingUtil.getCurrentClassWholeName(aClass);
                        if (Pattern.matches(line, currentClassWholeName)) {
                            //get all method
                            PsiMethod[] methods = aClass.getMethods();
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

            @Override
            public void visitMethod(PsiMethod method) {
                super.visitMethod(method);
                String line = "";
                final PsiClass currentFileClass = PsiTreeUtil.getParentOfType(method, PsiClass.class);
                try {
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

            @Override
            public void visitAssertStatement(PsiAssertStatement statement) {
                super.visitAssertStatement(statement);
                TOTAL_COUNT[RULE_ASSERT]++;
                if (statement.getAssertDescription() == null) {
                    MISS_COUNT[RULE_ASSERT]++;
                }
            }

            @Override
            public void visitIfStatement(PsiIfStatement statement) {
                super.visitIfStatement(statement);

                // 先获得if-else代码块  在里面写提示"if-else hhh"
                PsiExpression expression = statement.getCondition();

                //获取then分支
                PsiStatement thenbranch = statement.getThenBranch();

                //获取else分支
                PsiStatement elsebranch = statement.getElseBranch();
                if (expression == null || thenbranch == null || elsebranch == null) {
                    return;
                }

                TOTAL_COUNT[RULE_CRITICAL_BRANCH]++;
                boolean isThenRepeated, isElseRepeated = false;
                if (thenbranch instanceof PsiBlockStatement) {
                    isThenRepeated = findRepeatStatement(((PsiBlockStatement) thenbranch).getCodeBlock().getStatements());
                } else {
                    isThenRepeated = findRepeatStatement(thenbranch);
                }
                if (elsebranch instanceof PsiBlockStatement) {
                    isElseRepeated = findRepeatStatement(((PsiBlockStatement) elsebranch).getCodeBlock().getStatements());
                } else {
                    isElseRepeated = findRepeatStatement(elsebranch);
                }

                if (!isThenRepeated || !isElseRepeated) {
                    MISS_COUNT[RULE_CRITICAL_BRANCH]++;
                }
            }

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

            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                super.visitCallExpression(expression);
                if (isNullLiteral(expression)) {
                    return;
                }
                final PsiReferenceExpression methodExpression = expression.getMethodExpression();
                final String methodName = methodExpression.getReferenceName();
                //check method name
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
                            if (lConn != null && lConn.getType().toString().equals("PsiType:Connection")) {
                                final PsiReferenceExpression methodExpression = rGetConn.getMethodExpression();
                                final String methodName = methodExpression.getReferenceName();
                                //check right
                                //check method name
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

            //2. check execute
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
                            } else if (initializer instanceof PsiMethodCallExpression && initializer.getType() != null && initializer.getType().toString().equals("PsiType:boolean")) {
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
                            } else if (initializer instanceof PsiMethodCallExpression && initializer.getType() != null && initializer.getType().toString().equals("PsiType:int")) {
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

            //查重复
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

            private boolean isNullLiteral(PsiExpression expr) {
                return expr instanceof PsiLiteralExpression && "null".equals(expr.getText());
            }

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

            private boolean isToLog(PsiMethod method) {
                final PsiCodeBlock codeBlock = method.getBody();
                final PsiStatement[] statements = codeBlock.getStatements();
                if (statements.length == 0) return false;
                //check first
                if (!statements[0].getText().startsWith("log.")) {
                    return true;
                }
                //check returns
                boolean returnAllLogged = true;
                PsiType returnType = method.getReturnType();
                ;
                if (returnType == PsiType.VOID) {
                    return false;
                }

                PsiReturnStatement[] returnStatements = PsiUtil.findReturnStatements(method);
                for (PsiReturnStatement returnStatement : returnStatements) {
                    //check previous
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
