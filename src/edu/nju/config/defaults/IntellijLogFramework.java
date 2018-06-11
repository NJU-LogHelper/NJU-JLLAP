package edu.nju.config.defaults;

import edu.nju.config.LogLevel;

import java.util.*;

public class IntellijLogFramework extends LogFrameworkDefaults {
    IntellijLogFramework() {
        super("Intellij-IDEA");
        this.setDefaultLoggerFieldName("LOG");
        this.setLoggerClass("com.intellij.openapi.diagnostic.Logger");
        this.setLoggerFactoryMethod("com.intellij.openapi.diagnostic.Logger.getInstance(\"#%s\")");
        final List<LogLevel> noGetterLevels = new ArrayList<>(Arrays.asList(LogLevel.values()));
        noGetterLevels.remove(LogLevel.debug);
        this.getEnabledGetterMethod().keySet().removeAll(noGetterLevels);
    }
}
