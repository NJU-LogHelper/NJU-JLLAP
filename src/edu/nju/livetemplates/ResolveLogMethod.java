package edu.nju.livetemplates;

import org.jetbrains.annotations.*;
import com.intellij.codeInsight.template.*;

public class ResolveLogMethod extends AbstractMacro {
    public ResolveLogMethod() {
        super("resolveLogMethod");
    }

    public String getPresentableName() {
        return "ResolveLoggerMethod";
    }

    @Override
    public String getDescription() {
        return this.getName() + "(LOGGER, LogLevel)";
    }

    public Result calculateResult(@NotNull Expression[] expressions, ExpressionContext context) {
        Result method = null;
        if (expressions.length == 2) {
            String loggerClass = this.resolveVariableType(expressions[0], context);
            if (loggerClass == null) {
                loggerClass = ResolveLoggerInstance.getLastCreatedLoggerInstance();
            }
            String methodExpression = this.resolveLogMethodExpression(loggerClass, this.parseLogLevel(expressions[1], context));
            if (methodExpression != null) {
                if (!methodExpression.contains("(")) {
                    methodExpression += "(";
                }
                method = new TextResult(methodExpression);
            }
        }
        return method;
    }

}
