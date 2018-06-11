package edu.nju.config.Inspection.defaults;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="list")
public class LogContextList {
    List<LogContext> logContexts=new ArrayList<>();

    public LogContextList() {
    }

    public LogContextList(List<LogContext> logContexts) {
        this.logContexts = logContexts;
    }

    @XmlElement(name="LogContext")
    public List<LogContext> getLogContexts() {
        return logContexts;
    }

    public void setLogContexts(List<LogContext> logContexts) {
        this.logContexts = logContexts;
    }
}
