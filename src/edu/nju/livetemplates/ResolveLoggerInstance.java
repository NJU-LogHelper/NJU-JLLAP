package edu.nju.livetemplates;

import com.intellij.codeInsight.template.impl.TemplateState;
import com.intellij.openapi.diagnostic.*;
import edu.nju.config.LogFramework;
import edu.nju.config.persist.model.ProjectConfiguration;
import edu.nju.util.LogPsiUtil;
import edu.nju.util.LoggerFieldBuilder;
import org.jetbrains.annotations.*;
import com.intellij.codeInsight.template.*;
import com.intellij.codeInsight.lookup.*;

import java.util.*;

import com.intellij.psi.*;

public class ResolveLoggerInstance extends AbstractResolveMacro {
    private static final Logger LOG = Logger.getInstance("#edu.nju.livetemplates.ResolveLoggerInstance");
    private static volatile String lastCreatedLoggerInstance;

    public static String getLastCreatedLoggerInstance() {
        return ResolveLoggerInstance.lastCreatedLoggerInstance;
    }

    private static void setLastCreatedLoggerInstance(String lastCreatedLoggerInstance) {
        ResolveLoggerInstance.lastCreatedLoggerInstance = lastCreatedLoggerInstance;
    }

    public ResolveLoggerInstance() {
        super("resolveLoggerInstance");
    }

    public String getPresentableName() {
        return "ResolveLogger";
    }

    @NotNull
    @Override
    public String getDefaultValue() {
        return "log";
    }

    public Result calculateResult(@NotNull Expression[] expressions, ExpressionContext context) {
        PsiFile file = this.getPsiFile(context);
        assert file != null;
        ProjectConfiguration config = ProjectConfiguration.getInstance();
        PsiElement[] variables = this.resolveVariables(config.getSupportedLoggerClasses(), file, context);
        StringBuilder s= new StringBuilder();
        for(PsiElement p:variables){
            s.append(p.getText());
        }
        if (variables.length > 0) {
            return new JavaPsiElementResult(variables[0]);
        }
        return new TextResult(this.getDefaultValue());
    }

    @Override
    public LookupElement[] calculateLookupItems(@NotNull Expression[] expressions, ExpressionContext context) {
        PsiFile file = this.getPsiFile(context);
        assert file != null;
        ProjectConfiguration config = ProjectConfiguration.getInstance();
        PsiElement[] variables = this.resolveVariables(config.getSupportedLoggerClasses(), file, context);
        if (variables != null && variables.length >= 2) {
            return this.convertToLookupItems(variables);
        }
        return null;
    }

    @Override
    protected PsiElement[] resolveVariables(Set<String> stringTypes, PsiFile file, ExpressionContext context) {
        PsiElement place = this.getPlace(file, context);
        assert place != null;
        LogFramework framework = ProjectConfiguration.getInstance().getDefaultLogFramework();
        PsiElement[] variables = super.resolveVariables(stringTypes, file, context);
        StringBuilder s=new StringBuilder();
        assert variables != null;
        if (variables.length == 0) {
            PsiField field = this.createField(file, place);
            if (field != null) {
                setLastCreatedLoggerInstance(field.getType().getCanonicalText());
                variables = new PsiElement[]{field};
            }
        }
        return variables;
    }

    private PsiField createField(PsiFile file, PsiElement place) {
        if (!file.isWritable()) {
            return null;
        }
        LoggerFieldBuilder fieldBuilder = new LoggerFieldBuilder();
        PsiField field = fieldBuilder.createField(place);
        if (field != null) {
            Runnable runnable=fieldBuilder.createFieldInserter(place, field);
            TemplatePostProcessor.schedule(file, runnable, true);
        }
        return field;
    }

}
