package edu.nju.codeInspection;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.*;
import edu.nju.config.Inspection.defaults.LogContext;
import edu.nju.quickfixes.QuickFixFactory;
import edu.nju.quickfixes.QuickFixFactoryImpl;
import edu.nju.util.XmlUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class TotalInspection extends BaseJavaLocalInspectionTool {
    private QuickFixFactory factory=new QuickFixFactoryImpl();
    private static final Logger LOG = Logger.getInstance("#edu.nju.codeInspection.TotalInspection");
    private XmlUtil xu=new XmlUtil();
    private List<LogContext> logContexts=xu.getLogContextArrayList();
//    private static final String test="'InitializerDeclaration', ' TryStmt', ' CatchClause', ' LambdaExpr', ' ForeachStmt', '" +
//            " SwitchStmt', ' BlockStmt', ' WhileStmt', ' MethodCallExpr', ' ConstructorDeclaration', ' ObjectCreationExpr', ' SynchronizedStmt', " +
//            "' DoStmt', ' ForStmt', ' IfStmt', ' SwitchEntryStmt', ' VariableDeclarator', ' MethodDeclaration'";
    @NotNull
    public String getDisplayName() {
        return "自定义检测的问题";
    }

    @NotNull
    public String getGroupDisplayName() {
        return GroupNames.BUGS_GROUP_NAME;
    }

    @NotNull
    public String getShortName() {
        return "Custom";
    }

    private boolean checkKeyWords(String strToCheck,List<String> keywords){
        if(keywords==null||keywords.size()==0){
            return true;
        }
        for(String str:keywords){
            if(strToCheck.contains(str)){
                return true;
            }
        }
        return false;
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitReferenceExpression(PsiReferenceExpression psiReferenceExpression) {
            }

            @Override
            public void visitClassInitializer(PsiClassInitializer initializer) {
                //InitializerDeclaration
                super.visitClassInitializer(initializer);
            }

            @Override
            public void visitTryStatement(PsiTryStatement statement) {
                //TryStmt
                super.visitTryStatement(statement);
                PsiCodeBlock psiCodeBlock=statement.getTryBlock();
                PsiStatement[] psiStatements = psiCodeBlock.getStatements();
                if(!checkLogMethodExist(psiStatements)){
                    for(LogContext logContext:logContexts){
                        if(logContext.getTypeName().equals("TryStmt")){
                            List<String> keywords=logContext.getKeyWord();
                            String logMessage=logContext.getLogTemplate();
                            if(checkKeyWords(statement.getText(),keywords)){
                                holder.registerProblem(statement, logContext.getDescriptionTemplate(),
                                        factory.createTryStmtQuickFix(logContext.getDefaultLogLevel(),logMessage,logContext.getQuickFixName()));
                            }
                        }
                    }
                }
            }

            @Override
            public void visitIfStatement(PsiIfStatement statement) {
                //IfStmt
                super.visitIfStatement(statement);
                PsiCodeBlock psiCodeBlock = ((PsiBlockStatement) Objects.requireNonNull(statement.getThenBranch())).getCodeBlock();
                PsiStatement[] psiStatements = psiCodeBlock.getStatements();
                if(!checkLogMethodExist(psiStatements)){
                    for(LogContext logContext:logContexts){
                        if(logContext.getTypeName().equals("IfStmt")){
                            List<String> keywords=logContext.getKeyWord();
                            String logMessage=logContext.getLogTemplate();
                            if(checkKeyWords(statement.getText(),keywords)){
                                holder.registerProblem(statement, logContext.getDescriptionTemplate(),
                                        factory.createIfStmtQuickFix(logContext.getDefaultLogLevel(),logMessage,logContext.getQuickFixName()));
                            }
                        }
                    }
                }
            }

            @Override
            public void visitForeachStatement(PsiForeachStatement statement){
                super.visitForeachStatement(statement);
                PsiCodeBlock psiCodeBlock=((PsiBlockStatement)statement.getBody()).getCodeBlock();
                PsiStatement[] psiStatements = psiCodeBlock.getStatements();
                if(!checkLogMethodExist(psiStatements)){
                    for(LogContext logContext:logContexts){
                        if(logContext.getTypeName().equals("ForeachStmt")){
                            List<String> keywords=logContext.getKeyWord();
                            String logMessage=logContext.getLogTemplate();
                            if(checkKeyWords(psiCodeBlock.getText(),keywords)){
                                holder.registerProblem(psiCodeBlock, logContext.getDescriptionTemplate(),
                                        factory.createMethodDeclarationQuickFix(logContext.getDefaultLogLevel(),logMessage,logContext.getQuickFixName()));
                            }
                        }
                    }
                }
            }

            @Override
            public void visitMethod(PsiMethod psiMethod){
                super.visitMethod(psiMethod);
                PsiCodeBlock psiCodeBlock=psiMethod.getBody();
                PsiStatement[] psiStatements = psiCodeBlock.getStatements();
                if(!checkLogMethodExist(psiStatements)){
                    for(LogContext logContext:logContexts){
                        if(logContext.getTypeName().equals("MethodDeclaration")){
                            List<String> keywords=logContext.getKeyWord();
                            String logMessage=logContext.getLogTemplate();
                            if(checkKeyWords(psiMethod.getText(),keywords)){
                                holder.registerProblem(psiMethod, logContext.getDescriptionTemplate(),
                                        factory.createMethodDeclarationQuickFix(logContext.getDefaultLogLevel(),logMessage,logContext.getQuickFixName()));
                            }
                        }
                    }
                }
            }

            @Override
            public void visitCatchSection(PsiCatchSection section) {
                //CatchClause
                super.visitCatchSection(section);
                PsiCodeBlock psiCodeBlock = section.getCatchBlock();
                if (psiCodeBlock == null) {
                    return;
                }
                PsiStatement[] psiStatements = psiCodeBlock.getStatements();
                if(!checkLogMethodExist(psiStatements)){
                    for(LogContext logContext:logContexts){
                        if(logContext.getTypeName().equals("CatchClause")){
                            List<String> keywords=logContext.getKeyWord();
                            String logMessage=logContext.getLogTemplate();
                            if(checkKeyWords(section.getText(),keywords)){
                                holder.registerProblem(section, logContext.getDescriptionTemplate(),
                                        factory.createCatchClauseQuickFix(logContext.getDefaultLogLevel(),logMessage,logContext.getQuickFixName()));
                            }
                        }
                    }
                }
            }
        };
    }



    private boolean checkLogMethodExist(PsiStatement[] psiStatements){
        for(PsiStatement psiStatement:psiStatements){
            if(psiStatement instanceof PsiExpressionStatement){
                PsiExpression expression = ((PsiExpressionStatement) psiStatement).getExpression();
                if (expression instanceof PsiMethodCallExpression) {
                    PsiExpression qualifier=((PsiMethodCallExpression) expression).getMethodExpression().getQualifierExpression();
                    if(qualifier instanceof PsiReferenceExpression){
                        String text=qualifier.getText();
                        if(text.contains("LOG")||text.contains("log"))
                            return true;
                    }
                }
            }
        }
        return false;
    }

}
