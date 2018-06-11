package edu.nju.quickfixes;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class QuickFixFactoryImpl implements QuickFixFactory {

    private static final Logger LOG = Logger.getInstance("#edu.nju.quickfixes.QuickFixFactoryImpl");

    private void setLogMethod(PsiCodeBlock location,PsiExpressionStatement logStatement){
        Objects.requireNonNull(location.getFirstBodyElement()).replace(logStatement);
    }

    @Override
    public LocalQuickFix createCatchClauseQuickFix(String logLevel,String logMessage,String quickFixName) {
        String message;
        if(logMessage==null|| logMessage.equals("")){
            message= "Error while ...";
        }
        else{
            message= logMessage;
        }

        return new LocalQuickFix() {
            @NotNull
            public String getName() {
                return quickFixName;
            }

            @Nls
            @NotNull
            @Override
            public String getFamilyName() {
                return getName();
            }

            @Override
            public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
                PsiCatchSection psiCatchSection = (PsiCatchSection) problemDescriptor.getPsiElement();
                PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
                String expression="";
                if (psiCatchSection.getCatchType() != null && psiCatchSection.getParameter() != null) {
                    String exceptionName = psiCatchSection.getParameter().getName();
                    expression="log."+logLevel+"(\""+ message+"\","+exceptionName+");";
                }
                else{
                    expression="log."+logLevel+"(\""+ message+"\");";
                }
                PsiExpressionStatement logStatement= (PsiExpressionStatement) factory.createStatementFromText(expression, null);
                if(psiCatchSection.getCatchBlock()==null||psiCatchSection.getCatchBlock().getFirstBodyElement()==null){
                    LOG.error("catch语句不完整");
                    return;
                }
                setLogMethod(psiCatchSection.getCatchBlock(),logStatement);
            }
        };
    }

    @Override
    public LocalQuickFix createIfStmtQuickFix(String logLevel, String logMessage, String quickFixName) {
        String message;
        if(logMessage==null|| logMessage.equals("")){
            message= "get into a if statement";
        }
        else{
            message= logMessage;
        }

        return new LocalQuickFix() {

            @NotNull
            public String getName() {
                return quickFixName;
            }

            @Nls
            @NotNull
            @Override
            public String getFamilyName() {
                return getName();
            }

            @Override
            public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
                PsiIfStatement psiIfStatement= (PsiIfStatement) problemDescriptor.getPsiElement();
                PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
                String expression="";
                if(psiIfStatement.getCondition()==null||psiIfStatement.getCondition().getText().equals("")){
                    expression="log."+logLevel+"(\""+ message+"\");";
                }
                else{
                    expression="log."+logLevel+"(\""+ message+",with experssion:"+ psiIfStatement.getCondition().getText()+"\");";
                }
                PsiExpressionStatement logStatement= (PsiExpressionStatement) factory.createStatementFromText(expression, null);
                if(psiIfStatement.getThenBranch()==null){

                    LOG.error("if语句不完整");
                    return;
                }
                setLogMethod(((PsiBlockStatement)psiIfStatement.getThenBranch()).getCodeBlock(),logStatement);
            }
        };
    }

    @Override
    public LocalQuickFix createMethodDeclarationQuickFix(String logLevel, String logMessage, String quickFixName) {
        String message;
        if(logMessage==null|| logMessage.equals("")){
            message= "go into a methodDec";
        }
        else{
            message= logMessage;
        }

        return new LocalQuickFix() {
            @NotNull
            public String getName() {
                return quickFixName;
            }

            @Nls
            @NotNull
            @Override
            public String getFamilyName() {
                return getName();
            }

            @Override
            public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
                PsiMethod psiMethod=(PsiMethod)problemDescriptor.getPsiElement();
                PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
                String expression="";
                if(psiMethod.getName().equals("")){
                    expression="log."+logLevel+"(\""+ message+"\");";
                }
                else{
                    expression="log."+logLevel+"(\""+ message+",with methodName:"+ psiMethod.getName()+"\");";
                }
                PsiExpressionStatement logStatement= (PsiExpressionStatement) factory.createStatementFromText(expression, null);
                if(psiMethod.getBody()==null){

                    LOG.error("method语句不完整");
                    return;
                }
                setLogMethod(psiMethod.getBody(),logStatement);
            }
        };
    }

    @Override
    public LocalQuickFix createTryStmtQuickFix(String logLevel, String logMessage, String quickFixName) {
        String message;
        if(logMessage==null|| logMessage.equals("")){
            message= "try:";
        }
        else{
            message= logMessage;
        }
        return new LocalQuickFix() {
            @NotNull
            public String getName() {
                return quickFixName;
            }

            @Nls
            @NotNull
            @Override
            public String getFamilyName() {
                return getName();
            }

            @Override
            public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
                PsiTryStatement psiTryStatement= (PsiTryStatement) problemDescriptor.getPsiElement();
                PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
                String expression="";
                expression="log."+logLevel+"(\""+ message+"\");";
                PsiExpressionStatement logStatement= (PsiExpressionStatement) factory.createStatementFromText(expression, null);
                if(psiTryStatement.getTryBlock()==null){
                    LOG.error("try语句不完整");
                    return;
                }
                setLogMethod(psiTryStatement.getTryBlock(),logStatement);
            }
        };
    }

    @Override
    public LocalQuickFix createForeachStmtQuickFix(String logLevel, String logMessage, String quickFixName) {
        String message;
        if(logMessage==null|| logMessage.equals("")){
            message= "go into foreach loop:";
        }
        else{
            message= logMessage;
        }
        return new LocalQuickFix() {
            @NotNull
            public String getName() {
                return quickFixName;
            }

            @Nls
            @NotNull
            @Override
            public String getFamilyName() {
                return getName();
            }

            @Override
            public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
                PsiForeachStatement psiForeachStatement= (PsiForeachStatement) problemDescriptor.getPsiElement();
                PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
                String expression="";
                expression="log."+logLevel+"(\""+ message+"\");";
                PsiExpressionStatement logStatement= (PsiExpressionStatement) factory.createStatementFromText(expression, null);
                if(psiForeachStatement.getBody()==null){
                    LOG.error("foreach语句不完整");
                    return;
                }
                setLogMethod(((PsiBlockStatement)psiForeachStatement.getBody()).getCodeBlock(),logStatement);
            }
        };
    }
}
