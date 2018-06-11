package edu.nju.util.loggingutil;

/**
 * Created by chentiange on 2018/5/5.
 */
public enum LoggingType {
    JAVALOGGING, LOG4J, COMMONS_LOGGING, SLF4J;
    public String getFormatBeginner(LoggingType type){
        switch (type){
            case JAVALOGGING:
            case LOG4J:
                return "Logger.getLogger(\"";
            case COMMONS_LOGGING:
            case SLF4J:
                return "LoggerFactory.getLogger(\"";
            default:
                return "wrong logging type";
        }
    }
}
