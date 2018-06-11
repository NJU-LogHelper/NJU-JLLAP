package edu.nju.config.persist.model;

import com.intellij.openapi.components.*;
import edu.nju.config.LogFramework;
import edu.nju.config.defaults.LogFrameworkDefaults;
import edu.nju.config.defaults.LogFrameworkDefaultsList;
import edu.nju.config.persist.service.LogHelperProjectService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ProjectConfiguration {
    private String defaultFrameworkName;
    transient LogFrameworkDefaultsList logFrameworkDefaults = new LogFrameworkDefaultsList();

    public ProjectConfiguration() {
        this.defaultFrameworkName="log4j-1.2";
    }
    public static ProjectConfiguration getInstance(){
        return ServiceManager.getService(LogHelperProjectService.class).getProjectConfiguration();
    }


    public String getDefaultFrameworkName() {
        return defaultFrameworkName;
    }

    public void setDefaultFrameworkName(String defaultFrameworkName) {
        if(defaultFrameworkName==null){
            defaultFrameworkName="log4j-1.2";
        }
        this.defaultFrameworkName = defaultFrameworkName;
    }
    /**
     * Returns a set of supported logger class names.
     *
     * @return A set of logger names that are usable for logging.
     */
    @NotNull
    public Set<String> getSupportedLoggerClasses() {
        Set<String> classes = new LinkedHashSet<>();
        for(LogFrameworkDefaults logFrameworkDefaults:logFrameworkDefaults){
            classes.add(logFrameworkDefaults.getLoggerClass());
        }
        return classes;
    }
    /**
     * Returns true if the log configuration allows the usage of the named logger class.
     * Note: The class of the logger doesn't necessarily need to be the same as the configured
     * log default framework, for the case that reusing existing logger instances is enabled.
     *
     * @param className The classname to look for.
     * @return True if the logger class is supported for usage.
     */
    public boolean isSupportedLoggerClass(final String className) {
        return this.getSupportedLoggerClasses().contains(className);
    }


    /**
     * Returns the log framework for the given class if supported.
     *
     * @param loggerClassName The classname of the logger.
     * @param methodName      the method that was called.
     * @return the log framework for the given class if supported or 'null'
     * if the logger is not backed by a supported framework.
     */
    public LogFramework getSupportedFrameworkForLoggerClass(final String loggerClassName, final String methodName) {
        LogFramework match = null;
        LogFrameworkDefaultsList logFrameworkDefaultsList=new LogFrameworkDefaultsList();
        List<LogFramework> logFrameworkList = new ArrayList<>(logFrameworkDefaultsList);
        for (final LogFramework f : logFrameworkList) {
            if (f.getLoggerClass().equals(loggerClassName)) {
                match = f;
                for (final String methodFragment : f.getLogMethod().values()) {
                    if (methodFragment.contains(methodName)) {
                        return f;
                    }
                }
            }
        }
        return match;
    }

    public LogFrameworkDefaultsList getLogFrameworkDefaults(){
        return logFrameworkDefaults;
    }

    /**
     * Returns the default log framework to use.
     *
     * @return the default log framework to use.
     */
    @Nullable
    public LogFramework getDefaultLogFramework() {
        for(LogFrameworkDefaults logFrameworkDefaults:logFrameworkDefaults){
            if(defaultFrameworkName.equals(logFrameworkDefaults.getName())){
                return logFrameworkDefaults;
            }
        }
        return null;
    }

}
