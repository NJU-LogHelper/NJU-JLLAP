package edu.nju.util.loggingutil;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public class BranchStatementLoggingUtil {
    private static final Logger LOG = Logger.getInstance("#com.com.intellij.codeInspection.BranchStatementsInspection");

    public static void doBranchLogging(@NotNull Project project, @NotNull ProblemDescriptor descriptor, LoggingType type, Object level, int typeId) {
        try {

            //获取当下的元素
            PsiBinaryExpression binaryExpression = (PsiBinaryExpression) descriptor.getPsiElement();

            //获取分支主元素
            PsiIfStatement ifStatement = (PsiIfStatement) binaryExpression.getParent();

            //暂时支持单层的if-else
            PsiBlockStatement ifBlock = (PsiBlockStatement) binaryExpression.getNextSibling().getNextSibling();
            PsiBlockStatement elseBlock = (PsiBlockStatement)ifStatement.getLastChild();

            LogPsiBlockStatement(ifBlock,project,level,typeId);
            LogPsiBlockStatement(elseBlock,project,level,typeId);

        } catch (IncorrectOperationException e) {
            LOG.error(e);
        }
    }


    private static void LogPsiBlockStatement(PsiBlockStatement psiBlockStatement,Project project,Object level,int typeId){

        PsiCodeBlock psiCodeBlock = psiBlockStatement.getCodeBlock();
        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
        String levelString = LoggingUtil.getLevelStringById(level, typeId);
        String stmt="enter please_enter_your_branch_name";
        String logContent = String.format("%s %s %s\");","log.",levelString,stmt);

        PsiExpressionStatement logCall = (PsiExpressionStatement) factory.createStatementFromText(logContent,null);

        //如果有log分支，则不打印
        if(psiCodeBlock==null){
            LOG.error("psiCodeBlock is null");
        } else{
            /**
             * 如果block里面为空则无法插入
             * 修复
             */
//            PsiStatement psiStatement=psiCodeBlock.getStatements()[0];
            PsiStatement []psiStatements=psiCodeBlock.getStatements();
            if(psiStatements.length!=0){
                psiCodeBlock.addBefore(logCall,psiStatements[0]);
            }
            psiCodeBlock.add(logCall);
        }
    }

}
