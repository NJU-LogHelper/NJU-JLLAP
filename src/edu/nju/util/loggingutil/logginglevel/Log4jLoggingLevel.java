package edu.nju.util.loggingutil.logginglevel;

/**
 * Created by chentiange on 2018/5/5.
 */
public enum Log4jLoggingLevel {
    LOG_FATAL, LOG_ERROR, LOG_WARN, LOG_INFO, LOG_DEBUG, LOG_TRACE;

    public String getLevelString(Log4jLoggingLevel level){
        switch (level){
            case LOG_FATAL:
//                return "\").fatal(\"";
                return "fatal(\"";
            case LOG_ERROR:
                return "error(\"";
            case LOG_WARN:
                return "warn(\"";
            case LOG_INFO:
                return "info(\"";
            case LOG_DEBUG:
                return "debug(\"";
            case LOG_TRACE:
                return "trace(\"";
            default:
                return "wrong log4j logging level";
        }
    }

}
