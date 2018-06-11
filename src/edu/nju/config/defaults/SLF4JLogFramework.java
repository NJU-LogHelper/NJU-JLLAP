package edu.nju.config.defaults;


public class SLF4JLogFramework extends LogFrameworkDefaults
{
    SLF4JLogFramework() {
        super("slf4j");
        this.setLoggerClass("org.slf4j.Logger");
        this.setLoggerFactoryMethod("org.slf4j.LoggerFactory.getLogger(%s.class)");
    }
}
