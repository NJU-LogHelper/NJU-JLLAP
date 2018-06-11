package edu.nju.config;

import com.intellij.psi.PsiModifier;

import java.util.*;

public class LogFramework {
    /**
     * Lists all access modifiers that are valid to use with logger fields.
     */
    private static final Set<String> LOGGER_ACCESS_MODIFIERS = Collections.unmodifiableSet(new LinkedHashSet<>(
            Arrays.asList(PsiModifier.PRIVATE, PsiModifier.PACKAGE_LOCAL, PsiModifier.PROTECTED, PsiModifier.PUBLIC)));
    private String name;
    private String loggerClass;
    private String loggerFactoryMethod;
    private String defaultLoggerFieldName = "log";
    private boolean insertLoggerAtEndOfClass;
    private boolean logMethodsAreStatic;
    private String loggerFieldAccessModifier = PsiModifier.PRIVATE;
    private boolean useStaticLogger = true;
    private boolean useFinalLogger = true;
    private Map<LogLevel, String> logMethod=new HashMap<>();
    private Map<LogLevel, String> enabledGetterMethod=new HashMap<>();

    {
        // Setting defaults for method names and enabledGetters.
        for (LogLevel level : LogLevel.values()) {
            logMethod.put(level, level.name());
            enabledGetterMethod.put(level, String.format("is%sEnabled",
                    Character.toUpperCase(level.name().charAt(0)) + level.name().substring(1)));
        }
    }

    public LogFramework() {
    }

    public LogFramework(final String name) {
        this.name = name;
    }

    public void importSettings(final LogFramework other) {
        if (other == this) {
            return;
        }
        this.name = other.name;
        this.loggerClass = other.loggerClass;
        this.loggerFactoryMethod = other.loggerFactoryMethod;
        this.defaultLoggerFieldName = other.defaultLoggerFieldName;
        this.loggerFieldAccessModifier = other.loggerFieldAccessModifier;
        this.insertLoggerAtEndOfClass = other.insertLoggerAtEndOfClass;
        this.logMethodsAreStatic = other.logMethodsAreStatic;
        this.useFinalLogger = other.useFinalLogger;
        this.useStaticLogger = other.useStaticLogger;
        this.logMethod.clear();
        this.logMethod.putAll(other.logMethod);
        this.enabledGetterMethod.clear();
        this.enabledGetterMethod.putAll(other.enabledGetterMethod);
    }

    public LogFramework copy() {
        final LogFramework copy = new LogFramework();
        copy.importSettings(this);
        return copy;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof LogFramework)) return false;
        LogFramework framework = (LogFramework) o;
        if (name != null ? !name.equals(framework.name) : framework.name != null) return false;
        if (loggerClass != null ? !loggerClass.equals(framework.loggerClass) : framework.loggerClass != null)
            return false;
        if (loggerFactoryMethod != null ?
                !loggerFactoryMethod.equals(framework.loggerFactoryMethod) :
                framework.loggerFactoryMethod != null)
            return false;
        if (defaultLoggerFieldName != null ?
                !defaultLoggerFieldName.equals(framework.defaultLoggerFieldName) :
                framework.defaultLoggerFieldName != null)
            return false;
        if (!loggerFieldAccessModifier.equals(framework.loggerFieldAccessModifier)) return false;
        if (insertLoggerAtEndOfClass != framework.insertLoggerAtEndOfClass) return false;
        if (useFinalLogger != framework.useFinalLogger) return false;
        if (useStaticLogger != framework.useStaticLogger) return false;
        if (!logMethod.equals(framework.logMethod)) return false;
        return enabledGetterMethod.equals(framework.enabledGetterMethod);
    }

    @Override
    public int hashCode() {
        int result = (this.name != null) ? this.name.hashCode() : 0;
        result = 31 * result + ((this.loggerClass != null) ? this.loggerClass.hashCode() : 0);
        result = 31 * result + ((this.loggerFactoryMethod != null) ? this.loggerFactoryMethod.hashCode() : 0);
        result = 31 * result + ((this.defaultLoggerFieldName != null) ? this.defaultLoggerFieldName.hashCode() : 0);
        result = 31 * result + (this.insertLoggerAtEndOfClass ? 1 : 0);
        result = 31 * result + (this.logMethodsAreStatic ? 1 : 0);
        result = 31 * result + this.loggerFieldAccessModifier.hashCode();
        result = 31 * result + (this.useStaticLogger ? 1 : 0);
        result = 31 * result + (this.useFinalLogger ? 1 : 0);
        result = 31 * result + this.logMethod.hashCode();
        result = 31 * result + this.enabledGetterMethod.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "LogFramework{name='" + this.name + '\'' + ", loggerClass='" + this.loggerClass + '\'' + '}';
    }

    public String getName() {
        return (this.name == null) ? "" : this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getLoggerClass() {
        return (this.loggerClass == null) ? "" : this.loggerClass;
    }

    public void setLoggerClass(final String loggerClass) {
        this.loggerClass = loggerClass;
    }

    public String getLoggerFactoryMethod() {
        return this.loggerFactoryMethod;
    }

    public String getLoggerFactoryMethod(final String forClass) {
        try {
            return String.format((this.loggerFactoryMethod == null) ? "" : this.loggerFactoryMethod, forClass);
        } catch (IllegalFormatException e) {
            return this.loggerFactoryMethod;
        }
    }

    public void setLoggerFactoryMethod(final String loggerFactoryMethod) {
        this.loggerFactoryMethod = loggerFactoryMethod;
    }

    public boolean isUseStaticLogger() {
        return this.useStaticLogger;
    }

    public void setUseStaticLogger(final boolean useStaticLogger) {
        this.useStaticLogger = useStaticLogger;
    }

    public boolean isUseFinalLogger() {
        return this.useFinalLogger;
    }

    public void setUseFinalLogger(final boolean useFinalLogger) {
        this.useFinalLogger = useFinalLogger;
    }


    public boolean isInsertLoggerAtEndOfClass() {
        return this.insertLoggerAtEndOfClass;
    }

    public void setInsertLoggerAtEndOfClass(final boolean insertLoggerAtEndOfClass) {
        this.insertLoggerAtEndOfClass = insertLoggerAtEndOfClass;
    }

    public boolean isLogMethodsAreStatic() {
        return this.logMethodsAreStatic;
    }

    public void setLogMethodsAreStatic(final boolean logMethodsAreStatic) {
        this.logMethodsAreStatic = logMethodsAreStatic;
    }

    public String getDefaultLoggerFieldName() {
        return this.defaultLoggerFieldName;
    }

    public void setDefaultLoggerFieldName(final String defaultLoggerFieldName) {
        this.defaultLoggerFieldName = defaultLoggerFieldName;
    }

    public String getLoggerFieldAccessModifier() {
        return this.loggerFieldAccessModifier;
    }

    public void setLoggerFieldAccessModifier(final String loggerFieldAccessModifier) {
        if (!LogFramework.LOGGER_ACCESS_MODIFIERS.contains(loggerFieldAccessModifier)) {
            throw new IllegalArgumentException("The access modifier must be one of " + LogFramework.LOGGER_ACCESS_MODIFIERS);
        }
        this.loggerFieldAccessModifier = loggerFieldAccessModifier;
    }


    public Set<String> getDefaultLoggerFieldModifiers() {
        final Set<String> modifiers = new LinkedHashSet<>();
        modifiers.add(this.loggerFieldAccessModifier);
        if (this.useStaticLogger) {
            modifiers.add("static");
        }
        if (this.useFinalLogger) {
            modifiers.add("final");
        }
        return modifiers;
    }


    public Map<LogLevel, String> getLogMethod() {
        return this.logMethod;
    }

    public void setLogMethod(final Map<LogLevel, String> logMethod) {
        if (logMethod == null) {
            this.logMethod.clear();
        } else {
            this.logMethod = logMethod;
        }
    }

    public Map<LogLevel, String> getEnabledGetterMethod() {
        return this.enabledGetterMethod;
    }

    public void setEnabledGetterMethod(final Map<LogLevel, String> enabledGetterMethod) {
        if (enabledGetterMethod == null) {
            this.enabledGetterMethod.clear();
        } else {
            this.enabledGetterMethod = enabledGetterMethod;
        }
    }

    public boolean isLogFrameworkEquals(final LogFramework other) {
        return (this.getLoggerClass().equals(other.getLoggerClass()) && this.getLogMethod().equals(other.getLogMethod())) || this.getName().equalsIgnoreCase(other.getName());
    }

}
