package edu.nju.livetemplates;

import com.intellij.openapi.diagnostic.*;
import com.intellij.codeInsight.template.*;
import com.intellij.codeInsight.lookup.*;
import com.intellij.openapi.project.*;
import edu.nju.config.LogFramework;
import edu.nju.config.LogLevel;
import edu.nju.config.defaults.LogFrameworkDefaultsList;
import org.jetbrains.annotations.*;
import com.intellij.codeInsight.template.macro.*;
import com.intellij.psi.*;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMacro extends Macro {
    private static final Logger log = Logger.getInstance("#edu.nju.livetemplates.AbstractMacro");
    private String name;
    private LogFrameworkDefaultsList logFrameworkDefaultsList = new LogFrameworkDefaultsList();
    private List<LogFramework> logFrameworkList = new ArrayList<>(logFrameworkDefaultsList);

    AbstractMacro(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.getName() + "()";
    }

    @NonNls
    @NotNull
    public String getDefaultValue() {
        return "";
    }

    public Result calculateQuickResult(@NotNull Expression[] expressions, ExpressionContext context) {
        return null;
    }

    /**
     * Returns the psi file that the context refers to.
     *
     * @param context The context to evaluate.
     * @return the psi file that the context refers to.
     */
    @Nullable
    PsiFile getPsiFile(@NotNull ExpressionContext context) {
        Project project = context.getProject();
        return PsiDocumentManager.getInstance(project).getPsiFile(context.getEditor().getDocument());
    }

    /**
     * Returns the element under the caret of the expression context.
     *
     * @param file    the psi file that the context refers to.
     * @param context the context to evaluate.
     * @return the element under the caret of the expression context.
     */
    @Nullable
    PsiElement getPlace(PsiFile file, @NotNull ExpressionContext context) {
        int offset = context.getStartOffset();
        PsiDocumentManager.getInstance(file.getProject()).commitAllDocuments();
        return file.findElementAt(offset);
    }

    /**
     * Resolves the type of the variable, referenced by the given expression.
     *
     * @param expression the expression referencing a variable.
     * @param context    The context to evaluate against.
     * @return the type of the variable.
     */
    @Nullable
    String resolveVariableType(Expression expression, @NotNull ExpressionContext context) {
        Result result = expression.calculateResult(context);
        String variableName = String.valueOf(result);
        return this.resolveVariableType(variableName, context);
    }

    /**
     * Resolves the type of the variable, referenced by the given name.
     *
     * @param variableName the name of the variable under the context's scope.
     * @param context      The context to evaluate against.
     * @return the type of the variable.
     */
    @Nullable
    private String resolveVariableType(String variableName, @NotNull ExpressionContext context) {
        int offset = context.getStartOffset();
        PsiFile file = this.getPsiFile(context);
        PsiElement place = (file == null) ? null : file.findElementAt(offset);
        if (file != null && place != null) {
            for (PsiVariable variable : MacroUtil.getVariablesVisibleAt(place, variableName)) {
                String name = variable.getName();
                if (name != null && name.equals(variableName)) {
                    return variable.getType().getCanonicalText();
                }
            }
        }
        for (LogFramework framework : logFrameworkList) {
            if (framework.isLogMethodsAreStatic() && framework.getLoggerClass().equals(variableName)) {
                return variableName;
            }
        }
        return null;
    }

    /**
     * Resolves the callable expression to log under the given level.
     *
     * @param loggerClass the class containing the log method.
     * @param level       The level used to log.
     * @return An expression that can be used inside the log template, describing the method to call.
     */
    String resolveLogMethodExpression(String loggerClass, LogLevel level) {
        if (loggerClass != null) {
            for (LogFramework framework : logFrameworkList) {
                if (framework.getLoggerClass().equals(loggerClass)) {
                    return framework.getLogMethod().get(level);
                }
            }
            log.warn("Failed resolving a log method using logger class '" + loggerClass + "', using default method name instead.");
        }
        return level.name();
    }

    /**
     * Parses the log level contained in the given expression.
     *
     * @param expression The expression to parse the log level from.
     * @param context    The context to evaluate against.
     * @return The log level contained in the expression. Defaults to "info".
     */
    @NotNull
    LogLevel parseLogLevel(@NotNull Expression expression, ExpressionContext context) {
        LogLevel level = LogLevel.info;
        try {
            level = LogLevel.valueOf(String.valueOf(expression.calculateResult(context)));
        } catch (IllegalArgumentException e) {
            log.warn("Failed resolving log level from expression, using the default of '" + level + "'");
        }
        return level;
    }


}
