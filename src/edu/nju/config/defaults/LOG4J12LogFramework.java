package edu.nju.config.defaults;

import edu.nju.config.LogLevel;

import java.util.*;

public class LOG4J12LogFramework extends LogFrameworkDefaults {
    LOG4J12LogFramework() {
        super("log4j-1.2");
        this.setLoggerClass("org.apache.log4j.Logger");
        this.setLoggerFactoryMethod("org.apache.log4j.Logger.getLogger(%s.class)");
        this.getEnabledGetterMethod().keySet().removeAll(Arrays.asList(LogLevel.error, LogLevel.warn));
    }
}
