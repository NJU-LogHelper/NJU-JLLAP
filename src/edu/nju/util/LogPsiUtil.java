package edu.nju.util;

import com.intellij.openapi.diagnostic.*;
import com.intellij.lang.*;
import edu.nju.config.LogFramework;
import edu.nju.config.defaults.LogFrameworkDefaultsList;
import edu.nju.config.persist.model.ProjectConfiguration;
import org.jetbrains.annotations.*;

import java.lang.ref.*;
import java.util.*;

import com.intellij.openapi.editor.*;
import com.intellij.psi.util.*;
import com.intellij.psi.*;

public class LogPsiUtil {
    private static final Logger LOG;
    private static Reference<Map<String, PsiMethodCallExpression>> logLevelCache;
    private static Language groovy;
    private static final Class<? extends PsiElement>[] ignoredElementsInParamList;
    private static final Class<? extends PsiElement>[] ignoredElementsInBinaryExpression;

    /**
     * Returns the psi element factory used to create literal replacements.
     *
     * @param file The file to get the factory for.
     * @return An instance of element factory for the given project.
     */
    public static LogPsiElementFactory getFactory(@NotNull final PsiFile file) {
        if (LogPsiUtil.groovy != null && file.getLanguage().is(LogPsiUtil.groovy)) {
            return new LogPsiElementFactory() {
                PsiElementFactory factory = JavaPsiFacade.getInstance(file.getProject()).getElementFactory();
                private final PsiParserFacade psiParserFacade = PsiParserFacade.SERVICE.getInstance(file.getProject());

                @Override
                public PsiField createField(final String text, final PsiType type, final PsiElement context) {
                    return null;
                }

                @Override
                public PsiType createTypeFromText(final String text, final PsiElement context) {
                    return this.factory.createTypeElementFromText(text, context).getType();
                }

                @Override
                public PsiElement createExpressionFromText(final String text, final PsiElement context) {
                    return this.factory.createExpressionFromText(text, context);
                }

                @Override
                public PsiElement createStatementFromText(final String text, final PsiElement context) {
                    return this.factory.createStatementFromText(text, context);
                }

                @Override
                public PsiElement createWhiteSpaceFromText(final String text) {
                    return this.psiParserFacade.createWhiteSpaceFromText(text);
                }
            };
        } else {
            return new LogPsiElementFactory() {
                private final PsiElementFactory factory = JavaPsiFacade.getInstance(file.getProject()).getElementFactory();
                private final PsiParserFacade psiParserFacade = PsiParserFacade.SERVICE.getInstance(file.getProject());

                @Override
                public PsiField createField(final String text, final PsiType type, final PsiElement context) {
                    LOG.info("start factory create field");
                    return this.factory.createFieldFromText(type.getCanonicalText() + " " + text + ";", context);
                }

                @Override
                public PsiType createTypeFromText(final String text, final PsiElement context) {
                    return this.factory.createTypeFromText(text, context);
                }

                @Override
                public PsiElement createExpressionFromText(final String text, final PsiElement context) {
                    return this.factory.createExpressionFromText(text, context);
                }

                @Override
                public PsiElement createStatementFromText(final String text, final PsiElement context) {
                    return this.factory.createStatementFromText(text, context);
                }

                @Override
                public PsiElement createWhiteSpaceFromText(final String text) {
                    return this.psiParserFacade.createWhiteSpaceFromText(text);
                }
            };
        }
    }


//    /**
//     * Returns the configured log framework for the given PsiMethodCallExpression.
//     *
//     * @param expression the expression to lookup the configuration for.
//     * @return the configured log framework for the given PsiMethodCallExpression.
//     */
//    @Nullable
//    public static LogFramework getLogFramework(final PsiMethodCallExpression expression) {
//        final PsiExpression qualifier = expression.getMethodExpression().getQualifierExpression();
//        if (qualifier == null || !canBeLoggerCall(expression)) {
//            return null;
//        }
//        final PsiFile file = expression.getContainingFile();
//        final PsiType type = findSupportedLoggerType(file, qualifier.getType());
//        if (type != null) {
//            final String loggerClass = type.getCanonicalText();
//            final ProjectConfiguration configuration = ProjectConfiguration.getInstance();
//            final String methodName = expression.getMethodExpression().getReferenceName();
//            return configuration.getSupportedFrameworkForLoggerClass(loggerClass, methodName);
//        }
//        return null;
//    }

//    /**
//     * Compares 2 method calls and returns true if the full method is equivalent to the partial call.
//     *
//     * @param full    The full call.
//     * @param partial A subset of the full call to compare against.
//     * @return True if full contains the partial.
//     */
//    public static boolean isEquivalentTo(final PsiMethodCallExpression full, final PsiMethodCallExpression partial) {
//        if (partial.getMethodExpression().getText().equals(full.getMethodExpression().getText())) {
//            final Queue<PsiExpression> queue = new ArrayDeque<>(Arrays.asList(full.getArgumentList().getExpressions()));
//            for (final PsiExpression expression : partial.getArgumentList().getExpressions()) {
//                final PsiExpression e = queue.poll();
//                if (!"".equals(expression.getText())) {
//                    if (e == null || !expression.getText().endsWith(e.getText())) {
//                        return false;
//                    }
//                }
//            }
//            return true;
//        }
//        return false;
//    }

//    /**
//     * Returns all calls to log methods that are supported for processing.
//     *
//     * @param file The file to look for logger calls.
//     * @return all calls to log methods that are supported for processing.
//     */
//    @NotNull
//    public static List<PsiMethodCallExpression> findSupportedLoggerCalls(final PsiFile file) {
//        final PsiElement[] elements = PsiTreeUtil.collectElements(file, element -> element instanceof PsiMethodCallExpression && LogPsiUtil.isSupportedLoggerCall((PsiMethodCallExpression) element));
//        final List<PsiMethodCallExpression> calls = new ArrayList<>(elements.length);
//        for (final PsiElement element : elements) {
//            calls.add((PsiMethodCallExpression) element);
//        }
//        final List<PsiMethodCallExpression> list = calls;
//        return list;
//    }

//    public static boolean canBeLoggerCall(@NotNull final PsiMethodCallExpression callExpression) {
//        boolean canBeLogMethod = true;
//        final PsiReferenceExpression ref = callExpression.getMethodExpression();
//        final PsiElement lastChild = ref.getLastChild();
//        final String logMethodName = (lastChild == null) ? null : lastChild.getText();
//        if (logMethodName != null) {
//            LogFrameworkDefaultsList logFrameworkDefaultsList=new LogFrameworkDefaultsList();
//            final List<LogFramework> frameworks = new ArrayList<>(logFrameworkDefaultsList);
//            if (!frameworks.isEmpty()) {
//                canBeLogMethod = false;
//                for (final LogFramework framework : frameworks) {
//                    for (final String methodFragment : framework.getLogMethod().values()) {
//                        if (canBeLogMethod = methodFragment.contains(logMethodName)) {
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//        return canBeLogMethod;
//    }

//    /**
//     * Returns true if the given method call expression represents a supported call to a log method.
//     *
//     * @param callExpression the call expression to test.
//     * @return	'true' if the call expression can be supported.
//     */
//    public static boolean isSupportedLoggerCall(final PsiMethodCallExpression callExpression) {
//        final PsiReferenceExpression ref = callExpression.getMethodExpression();
//        if (ref.isQualified() && canBeLoggerCall(callExpression)) {
//            final PsiExpression qualifierExpression = ref.getQualifierExpression();
//            if (qualifierExpression != null) {
//                return findSupportedLoggerType(qualifierExpression.getContainingFile(), qualifierExpression) != null;
//            }
//        }
//        return false;
//    }

//    /**
//     * Returns true if the given logger type is a supported logger.
//     *
//     * @param file	   The file instance containing the logger.
//     * @param loggerType The type of the logger that received the logger call.
//     * @return	'true' if the logger class can be supported.
//     */
//    public static boolean isSupportedLoggerCall(@NotNull final PsiFile file, final PsiType loggerType) {
//        return findSupportedLoggerType(file, loggerType) != null;
//    }


//    @Nullable
//    public static PsiType findSupportedLoggerType(@NotNull final PsiFile file, @Nullable final PsiExpression qualifierExpression) {
//        if (qualifierExpression != null) {
//            PsiType type = qualifierExpression.getType();
//            if (type == null && qualifierExpression instanceof PsiReferenceExpression) {
//                final PsiElement resolved = ((PsiReferenceExpression) qualifierExpression).resolve();
//                if (resolved instanceof PsiClass) {
//                    type = getFactory(file).createTypeFromText(((PsiClass) resolved).getQualifiedName(), resolved);
//                }
//            }
//            return findSupportedLoggerType(file, type);
//        }
//        return null;
//    }
//    /**
//     * Returns the target type of a supported logger class.
//     *
//     * @param file	   The file instance containing the logger.
//     * @param loggerType The type of the logger that received the logger call.
//     * @return the target type of the logger or 'null' if the given type is not supported.
//     */
//    @Nullable
//    public static PsiType findSupportedLoggerType(@NotNull final PsiFile file, @Nullable final PsiType loggerType) {
//        if (loggerType != null) {
//            List<PsiType> superTypes;
//            for (List<PsiType> types = Collections.singletonList(loggerType); !types.isEmpty(); types = superTypes) {
//                for (final PsiType type : types) {
//                    final String loggerClass = type.getCanonicalText();
//                    if (isSupportedLoggerClass(file, loggerClass)) {
//                        return type;
//                    }
//                }
//                superTypes = new ArrayList<>(types.size() * 2);
//                for (final PsiType type2 : types) {
//                    superTypes.addAll(Arrays.asList(type2.getSuperTypes()));
//                }
//            }
//        }
//        return null;
//    }
//    /**
//     * Returns true if the given logger classname is a supported logger.
//     *
//     * @param file	  The file instance containing the logger.
//     * @param className The classname of the logger that received the logger call.
//     * @return	'true' if the logger class can be supported.
//     */
//    public static boolean isSupportedLoggerClass(@NotNull final PsiFile file, final String className) {
//        return ProjectConfiguration.getInstance().isSupportedLoggerClass(className);
//    }

//    @Nullable
//    public static PsiMethodCallExpression findSupportedMethodCallExpression(final Editor editor, final PsiFile file) {
//        final PsiMethodCallExpression callExpression = findMethodCallExpressionAtCaret(editor, file);
//        return (callExpression == null || !isSupportedLoggerCall(callExpression)) ? null : callExpression;
//    }

//    /**
//     * Finds and returns a supported method call expression under the current caret.
//     *
//     * @param editor The editor to retrieve the caret position from.
//     * @param file   The underlying parsed file.
//     * @return The supported expression instance or 'null' if not found.
//     */
//    @Nullable
//    public static PsiMethodCallExpression findMethodCallExpressionAtCaret(final Editor editor, final PsiFile file) {
//        final PsiElement psiUnderCaret = PsiUtil.getElementAtOffset(file, editor.getCaretModel().getOffset());
//        return findElement(iterateParents(psiUnderCaret), PsiMethodCallExpression.class, PsiExpressionList.class, PsiBinaryExpression.class, PsiReferenceExpression.class, PsiIdentifier.class, PsiVariable.class, PsiLiteralExpression.class, PsiJavaToken.class);
//    }

//    /**
//     * Finds and returns a supported literal expression under the current caret.
//     *
//     * @param editor The editor to retrieve the caret position from.
//     * @param file   The underlying parsed file.
//     * @return The supported expression instance or 'null' if not found.
//     */
//    public static PsiLiteralExpression findSupportedLiteralExpression(final Editor editor, final PsiFile file) {
//        final PsiMethodCallExpression callExpression = findSupportedMethodCallExpression(editor, file);
//        if (callExpression != null) {
//            return findSupportedLiteralExpression(callExpression.getArgumentList());
//        }
//        return null;
//    }
//    /**
//     * Finds and returns a supported literal expression in the given expression list.
//     *
//     * @param expressionList The expression list containing the literal expression.
//     * @return The supported expression instance or 'null' if not found.
//     */
    @SuppressWarnings("unchecked")
//    public static PsiLiteralExpression findSupportedLiteralExpression(final PsiExpressionList expressionList) {
//        if (expressionList == null) {
//            return null;
//        }
//        PsiElement firstChild = expressionList.getFirstChild();
//        Class<? extends PsiElement>[] elementIgnoreList = LogPsiUtil.ignoredElementsInParamList;
//        PsiLiteralExpression firstExpression = null;
//        while (firstChild != null) {
//            firstExpression = findElement(iterateSiblings(firstChild, true), PsiLiteralExpression.class, elementIgnoreList);
//            // If the literal expression is not inside the expression list, it may be contained
//            // inside a binary expression, if so, we retry.
//            if (firstExpression == null) {
//                final PsiBinaryExpression binaryExpression = findElement(iterateSiblings(firstChild, true), PsiBinaryExpression.class, elementIgnoreList);
//                if (binaryExpression != null) {
//                    firstChild = binaryExpression.getFirstChild();
//                    elementIgnoreList = LogPsiUtil.ignoredElementsInBinaryExpression;
//                } else {
//                    firstChild = null;
//                }
//            } else {
//                firstChild = null;
//            }
//        }
//        if (firstExpression != null && firstExpression.isValid() && firstExpression.getText().startsWith("\"")) {
//            return firstExpression;
//        }
//        return null;
//    }


    /**
     * Finds an specific element type inside a given iteration.
     *
     * @param elements			The elements to iterate.
     * @param expectedType		The expected type to look for.
     * @param ignoredElementTypes A set of types that may be skipped in the Iterable.
     * @param <E>                 The type of the PsiElement to return.
     * @return The first occurrence of the expected type or 'null' if not found.
     */
//    @SuppressWarnings("unchecked")
//    public static <E extends PsiElement> E findElement(final Iterable<PsiElement> elements, final Class<E> expectedType, final Class<? extends PsiElement>... ignoredElementTypes) {
//        mainLoop:
//        for (final PsiElement e : elements) {
//            final Class<? extends PsiElement> elementClass = e.getClass();
//            if (expectedType.isAssignableFrom(elementClass)) {
//                return (E) e;
//            }
//            for (final Class<? extends PsiElement> ignoredElement : ignoredElementTypes) {
//                if (ignoredElement != null && ignoredElement.isAssignableFrom(elementClass)) {
//                    continue mainLoop;
//                }
//            }
//            break;
//        }
//        return null;
//    }

    /**
     * Resolves the variable initializer behind the given variable reference expression.
     *
     * @param referenceExpression the reference expression to resolve.
     * @return the variable initializer of the variable behind the resolved reference.
     */
    @Nullable
    public static PsiExpression resolveVariableInitializer(final PsiReferenceExpression referenceExpression) {
        final PsiReference reference = referenceExpression.getReference();
        final PsiElement variable = (reference == null) ? null : reference.resolve();
        if (variable instanceof PsiVariable) {
            return resolveVariableInitializer((PsiVariable) variable);
        }
        return null;
    }

    /**
     * Resolves the variable initializer behind the given variable reference expression.
     *
     * @param variable the reference expression to resolve.
     * @return the variable initializer of the variable behind the resolved reference.
     */
    @Nullable
    public static PsiExpression resolveVariableInitializer(final PsiVariable variable) {
        if (variable == null) {
            return null;
        }
        final PsiExpression initializer = variable.getInitializer();
        if (initializer instanceof PsiReferenceExpression) {
            return resolveVariableInitializer((PsiReferenceExpression) initializer);
        }
        return initializer;
    }

    /**
     * Creates an iterable iterating all parents of the given element.
     *
     * @param element The element to use for iterating the parent tree.
     * @return An Iterable starting from the given element.
     */
//    public static Iterable<PsiElement> iterateParents(final PsiElement element) {
//        return new Iterable<>() {
//            @Override
//            public Iterator<PsiElement> iterator() {
//                return new Iterator<>() {
//                    PsiElement el = element;
//
//                    @Override
//                    public boolean hasNext() {
//                        return this.el != null;
//                    }
//
//                    @Override
//                    public PsiElement next() {
//                        try {
//                            return this.el;
//                        } finally {
//                            this.el = this.el.getParent();
//                        }
//                    }
//
//                    @Override
//                    public void remove() {
//                        throw new UnsupportedOperationException();
//                    }
//                };
//            }
//        };
//    }

//    private static Iterable<PsiElement> iterateSiblings(final PsiElement element, final boolean forward) {
//        return new Iterable<>() {
//            @NotNull
//            @Override
//            public Iterator<PsiElement> iterator() {
//                return new Iterator<>() {
//                    PsiElement el = element;
//
//                    @Override
//                    public boolean hasNext() {
//                        return this.el != null;
//                    }
//
//                    @Override
//                    public PsiElement next() {
//                        try {
//                            return this.el;
//                        } finally {
//                            this.el = (forward ? this.el.getNextSibling() : this.el.getPrevSibling());
//                        }
//                    }
//
//                    @Override
//                    public void remove() {
//                        throw new UnsupportedOperationException();
//                    }
//                };
//            }
//        };
//    }

//    @Nullable
//    public static Object computeConstantExpression(final PsiExpression expression) {
//        return computeConstantExpression(expression, false);
//    }

//    @Nullable
//    private static Object computeConstantExpression(final PsiExpression expression, final boolean throwExceptionOnOverflow) {
//        final JavaPsiFacade facade = JavaPsiFacade.getInstance(expression.getProject());
//        final PsiConstantEvaluationHelper helper = facade.getConstantEvaluationHelper();
//        try {
//            try {
//                return ReflectionUtil.invoke(helper, "computeConstantExpression", expression, throwExceptionOnOverflow);
//            } catch (NoSuchMethodError e) {
//                if (throwExceptionOnOverflow) {
//                    throw e;
//                }
//                return ReflectionUtil.invoke(helper, "computeConstantExpression", expression);
//            }
//        } catch (Exception e2) {
//            LogPsiUtil.LOG.warn("Failed to invoke computeConstantExpression() via reflection, using unsafe direct call (may break in some IDEA versions).");
//            return null;
//        }
//    }
//
    static {
        LOG = Logger.getInstance("#edu.nju.util.LogPsiUtil");
        LogPsiUtil.logLevelCache = new SoftReference<>(null);
        try {
            for (final Language language : Language.getRegisteredLanguages()) {
                if ("groovy".equals(language.getID())) {
                    LogPsiUtil.groovy = language;
                }
            }
        } catch (Throwable t) {
        }
        ignoredElementsInParamList = new Class[]{PsiJavaToken.class, PsiWhiteSpace.class, PsiAnnotation.class, PsiComment.class, PsiReferenceExpression.class};
        ignoredElementsInBinaryExpression = new Class[]{PsiJavaToken.class, PsiWhiteSpace.class, PsiAnnotation.class, PsiComment.class};
    }

}
