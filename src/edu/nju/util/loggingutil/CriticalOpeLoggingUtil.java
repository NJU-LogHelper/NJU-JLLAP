package edu.nju.util.loggingutil;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;


/**
 * Created by chentiange on 2018/5/8.
 */
public class CriticalOpeLoggingUtil {
    private static final Logger LOG = Logger.getInstance("#com.intellij.codeInspection.inspections.CriticalOpeInspection");
    public static void doCriticalOpeLogging(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor, LoggingType type, Object level, int typeId){
        try {
            PsiMethod psiMethod = (PsiMethod) problemDescriptor.getPsiElement();


            PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
            //get current class
            final PsiClass currentFileClass = PsiTreeUtil.getParentOfType(psiMethod, PsiClass.class);
            final String currentClassWholeName = LoggingUtil.getCurrentClassWholeName(currentFileClass);
            final String methodWholeName = currentClassWholeName+":"+psiMethod.getName();
            String stmt = methodWholeName+" be called with parameters: ";
            PsiParameterList psiParameterList = psiMethod.getParameterList();
            PsiParameter[] psiParameters = psiParameterList.getParameters();

            for (PsiParameter psiParameter:psiParameters){
                stmt = stmt + "\"+"+psiParameter.getName()+"+\", ";
            }
            if (psiParameters.length == 0){
                stmt = methodWholeName+" be called without parameter.";
            }else {
                stmt = stmt.substring(0,stmt.length()-2);
            }

            String levelString = LoggingUtil.getLevelStringById(level, typeId);

            String logContent = String.format("%s %s %s\");","log.",levelString,stmt);
            PsiExpressionStatement logstmt = (PsiExpressionStatement) factory.createStatementFromText(logContent,null);

            PsiCodeBlock methodBody = psiMethod.getBody();
            if (methodBody != null){
                final PsiStatement[] oldStatements = methodBody.getStatements();
                if (oldStatements.length > 0){
                    PsiStatement firstStatement = oldStatements[0];
                    if (!firstStatement.getText().startsWith("log.")){
//                        psiMethod.addBefore(logstmt,firstStatement);
                        firstStatement.getPrevSibling().replace(logstmt);
                    }

                    //add return
                    PsiType returnType = psiMethod.getReturnType();
                    if (returnType != PsiType.VOID){
                        PsiReturnStatement[] returnStatements = PsiUtil.findReturnStatements(psiMethod);
                        for (PsiReturnStatement returnStatement:returnStatements){
                            String returnstr = returnStatement.getText();
                            returnstr = returnstr.replace("return ","");
                            returnstr = returnstr.replace(";","");
                            String returnstmt = methodWholeName+" return \"+"+returnstr+"+\"";
                            String returnlogContent = String.format("%s  %s %s\");","log.",levelString,returnstmt);


                            PsiExpressionStatement returnst = (PsiExpressionStatement) factory.createStatementFromText(returnlogContent,null);
                            if (!returnStatement.getPrevSibling().getPrevSibling().getText().startsWith("log.")){
//                                psiMethod.addBefore(returnst,returnStatement);
                                returnStatement.getPrevSibling().replace(returnst);
                            }

                        }

                    }
                }
            }
        } catch (IncorrectOperationException e) {
            LOG.error(e);
        }

    }
}
