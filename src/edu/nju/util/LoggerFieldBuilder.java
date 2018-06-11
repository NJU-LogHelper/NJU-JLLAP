package edu.nju.util;

import com.intellij.openapi.diagnostic.*;
import com.intellij.psi.util.*;
import com.jgoodies.common.base.*;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.project.*;
import com.intellij.psi.codeStyle.*;
import com.intellij.util.*;
import com.intellij.psi.*;
import edu.nju.config.LogFramework;
import edu.nju.config.persist.model.ProjectConfiguration;
import org.jetbrains.annotations.*;

import java.util.function.Consumer;

public class LoggerFieldBuilder {
    private static final Logger LOG = Logger.getInstance("#edu.nju.util.LoggerFieldBuilder");


    private PsiClass classForPlace(PsiElement place) {
        return (place instanceof PsiClass) ? (PsiClass) place : PsiUtil.getTopLevelClass(place);
    }

    /**
     * Creates the logger field for the context of the specified place,
     * using the configured default logger implementation.
     *
     * @param place The place to use as reference, either the top level class or a child of it.
     * @return an instance of PsiField that may be used inside the top-level class of the given place.
     */
    public PsiField createField(PsiElement place) {
        PsiClass cls = this.classForPlace(place);
        LogFramework framework = ProjectConfiguration.getInstance().getDefaultLogFramework();
        PsiField field;
        if (framework != null) {
            LogPsiElementFactory factory = LogPsiUtil.getFactory(place.getContainingFile());
            int fieldNameSequence = 1;
            String fieldName = framework.getDefaultLoggerFieldName();
            while (!PsiUtil.isVariableNameUnique(fieldName, place))
                fieldName = framework.getDefaultLoggerFieldName() + (fieldNameSequence++);
            field = factory.createField(fieldName, factory.createTypeFromText(framework.getLoggerClass(), place.getContext()), place.getContext());
            if (field != null) {
                LOG.info("field: "+field.getText());
                if (!cls.isInterface()) {
                    PsiModifierList modifierList = field.getModifierList();
                    if (modifierList != null) {
                        framework.getDefaultLoggerFieldModifiers().forEach(new Consumer<String>() {
                            @Override
                            public void accept(String modifier) {
                                /**
                                 * Checks if it is possible to add or remove the specified modifier to the modifier list,
                                 * and throws an exception if the operation is not possible. Does not actually modify
                                 * anything.
                                 *
                                 * @param name  the name of the modifier to check the add or remove possibility for.
                                 * @param value true if the modifier should be added, false if it should be removed.
                                 * @throws IncorrectOperationException if the modification fails for some reason.
                                 */
                                modifierList.setModifierProperty(modifier, true);
                            }
                        });
                    }
                }
                String loggerFactoryMethod = framework.getLoggerFactoryMethod(cls.getQualifiedName());
                if (Strings.isNotBlank(loggerFactoryMethod)) {
                    field.setInitializer((PsiExpression) factory.createExpressionFromText(loggerFactoryMethod, cls.getContext()));
                }
            }
        } else {
            field = null;
        }
        return field;
    }

    /**
     * Creates a runnable that modifies the top-level class of the given place by adding a logger field.
     *
     * @param place The place to use as reference, either the top level class or a child of it.
     * @param field The field to add.
     * @return a runnable that modifies the top-level class of the given place by adding a logger field.
     */
    public Runnable createFieldInserter(PsiElement place, PsiField field) {
        PsiFile file = place.getContainingFile();
        Project project = file.getProject();
        PsiDocumentManager manager = PsiDocumentManager.getInstance(project);
        Document document = manager.getDocument(file);
        if (document != null) {
            return new Runnable() {
                @Override
                public void run() {
                    manager.commitDocument(document);
                    try {
                        PsiClass cls = classForPlace(place);
                        PsiElement brace = ReflectionUtil.invoke(cls, "getLBrace", new Object[0]);
                        LogPsiElementFactory factory = LogPsiUtil.getFactory(place.getContainingFile());
                        ProjectConfiguration config = ProjectConfiguration.getInstance();
                        LogFramework framework = config.getDefaultLogFramework();

                        if (framework != null) {
                            PsiElement addedField;
                            PsiField[] allFields = cls.getFields();
                            if (allFields.length == 0) {
                                addedField = cls.addAfter(field, brace);
                                cls.addAfter(factory.createWhiteSpaceFromText("\n\n\t"), brace);
                            } else addedField = addFieldBeforeAnchor(factory, cls, field, allFields[0]);
                            this.shortenFQNames(addedField);
                        }
                    } finally {
                        manager.doPostponedOperationsAndUnblockDocument(document);
                    }
                }

                void shortenFQNames(PsiElement elementToFormat) {
                    try {
                        JavaCodeStyleManager javaStyle = JavaCodeStyleManager.getInstance(project);
                        javaStyle.shortenClassReferences(elementToFormat);
                    } catch (IncorrectOperationException e) {
                        LoggerFieldBuilder.LOG.error(e);
                    }
                }
            };
        }
        return null;
    }

    private static PsiElement addFieldBeforeAnchor(@NotNull LogPsiElementFactory factory, @NotNull PsiClass cls, @NotNull PsiField field, @NotNull PsiElement anchor) {
        cls.addBefore(factory.createWhiteSpaceFromText("\n\t"), anchor);
        PsiElement addedField = cls.addBefore(field, anchor);
        cls.addBefore(factory.createWhiteSpaceFromText("\n"), anchor);
        return addedField;
    }

}
