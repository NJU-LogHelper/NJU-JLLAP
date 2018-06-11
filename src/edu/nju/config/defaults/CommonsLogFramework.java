package edu.nju.config.defaults;

public class CommonsLogFramework extends LogFrameworkDefaults
{
    CommonsLogFramework() {
        super("commons-log");
        this.setLoggerClass("org.apache.commons.logging.Log");
        this.setLoggerFactoryMethod("org.apache.commons.logging.LogFactory.getLog(%s.class)");
    }
}
