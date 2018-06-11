package edu.nju.util.loggingutil.logginglevel;

/**
 * Created by chentiange on 2018/5/5.
 */
public enum JavaLoggingLevel {
    LOG_FINEST, LOG_FINER, LOG_FINE, LOG_CONFIG, LOG_INFO, LOG_WARNING, LOG_SEVERE;

    public String getLevelString(JavaLoggingLevel level){
        switch (level){
            case LOG_FINEST:
                return "finest(\"";
            case LOG_FINER:
                return "finer(\"";
            case LOG_FINE:
                return "fine(\"";
            case LOG_CONFIG:
                return "config(\"";
            case LOG_INFO:
                return "info(\"";
            case LOG_WARNING:
                return "warning(\"";
            case LOG_SEVERE:
                return "severe(\"";
            default:
                return "wrong java logging level";
        }
    }
}
