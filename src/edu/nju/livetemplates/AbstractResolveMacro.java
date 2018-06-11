package edu.nju.livetemplates;

import com.intellij.openapi.diagnostic.Logger;
import edu.nju.util.LogPsiElementFactory;
import edu.nju.util.LogPsiUtil;
import edu.nju.util.ReflectionUtil;
import org.jetbrains.annotations.*;
import com.intellij.codeInsight.lookup.*;
import com.intellij.codeInsight.template.impl.*;
import com.intellij.codeInsight.template.*;

import java.util.*;

import com.intellij.codeInsight.template.macro.*;
import com.intellij.psi.*;
import com.intellij.openapi.util.*;

public abstract class AbstractResolveMacro extends AbstractMacro {

    private static final Logger LOG = Logger.getInstance("#edu.nju.livetemplates.AbstractResolveMacro");

    AbstractResolveMacro(final String name) {
        super(name);
    }

    @Nullable
    LookupElement[] convertToLookupItems(@Nullable final PsiElement[] variables) {
        if (variables == null) {
            return null;
        }
        final Set set = new LinkedHashSet();
        for (final PsiElement variable : variables) {
            ReflectionUtil.invoke(JavaTemplateUtil.class, "addElementLookupItem", set, variable);
        }
        int i = 0;
        final LookupElement[] elements = new LookupElement[set.size()];
        for (final Object o : set) {
            elements[i++] = (LookupElement) o;
        }
        return elements;
    }

    @Nullable
    protected PsiElement[] resolveVariables(final Set<String> stringTypes, final PsiFile file, final ExpressionContext context) {

        PsiDocumentManager.getInstance(context.getProject()).commitAllDocuments();
        final PsiElement place = this.getPlace(file, context);
        final Set<PsiType> types = new LinkedHashSet<>(stringTypes.size());
        final LogPsiElementFactory factory = LogPsiUtil.getFactory(file);
        for (final String type : stringTypes) {
            types.add(factory.createTypeFromText(type, place.getContext()));
        }
        final ArrayList<PsiElement> elementsInScope = new ArrayList<>();
        final PsiVariable[] variablesVisible;
        variablesVisible = MacroUtil.getVariablesVisibleAt(place, "");
        for (final PsiVariable var : variablesVisible) {
            main:
            {
                if (var instanceof PsiLocalVariable) {
                    final TextRange range = Objects.requireNonNull(var.getNameIdentifier()).getTextRange();
                    if (range != null && range.contains(context.getStartOffset())) {
                        break main;
                    }
                }
                for (final PsiType type2 : types) {
                    if (type2 == null || type2.isAssignableFrom(var.getType())) {
                        /**
                         * @return true if values of type {@code type} can be assigned to rvalues of this type.
                         * type2.isAssignableFrom(var.getType())
                         */
                        elementsInScope.add(var);
                        break;
                    }
                }
            }
        }
        return elementsInScope.toArray(new PsiElement[elementsInScope.size()]);
    }
}
