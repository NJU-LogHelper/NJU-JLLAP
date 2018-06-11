package edu.nju.util.loggingutil.logginglevel;

/**
 * Created by chentiange on 2018/5/6.
 */
public enum Slf4JLoggingLevel {
    LOG_ERROR, LOG_WARN, LOG_INFO, LOG_DEBUG, LOG_TRACE;
    public String getLevelString(Slf4JLoggingLevel level){
        switch (level){
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
                return "wrong slf4j logging level";
        }
    }
}
