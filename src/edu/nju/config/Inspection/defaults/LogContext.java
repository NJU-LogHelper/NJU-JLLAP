package edu.nju.config.Inspection.defaults;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class LogContext {
    private String typeName;
    private String descriptionTemplate;
    private String quickFixName;
    private String logTemplate;
    private String defaultLogLevel;
    private List<String> keyWord;

    @Override
    public String toString() {
        return "LogContext{" +
                "typeName='" + typeName + '\'' +
                ", descriptionTemplate='" + descriptionTemplate + '\'' +
                ", quickFixName='" + quickFixName + '\'' +
                ", logTemplate='" + logTemplate + '\'' +
                ", defaultLogLevel='" + defaultLogLevel + '\'' +
                ", keyWord=" + keyWord +
                '}';
    }

    public LogContext(String typeName, String descriptionTemplate, String quickFixName, String logTemplate, String defaultLogLevel, List<String> keyWord) {
        this.typeName = typeName;
        this.descriptionTemplate = descriptionTemplate;
        this.quickFixName = quickFixName;
        this.logTemplate = logTemplate;
        this.defaultLogLevel = defaultLogLevel;
        this.keyWord = keyWord;
    }

    @XmlElement(name="typeName")
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @XmlElement(name="descriptionTemplate")
    public String getDescriptionTemplate() {
        return descriptionTemplate;
    }

    public void setDescriptionTemplate(String descriptionTemplate) {
        this.descriptionTemplate = descriptionTemplate;
    }

    @XmlElement(name="quickFixName")
    public String getQuickFixName() {
        return quickFixName;
    }

    public void setQuickFixName(String quickFixName) {
        this.quickFixName = quickFixName;
    }

    @XmlElement(name="logTemplate")
    public String getLogTemplate() {
        return logTemplate;
    }

    public void setLogTemplate(String logTemplate) {
        this.logTemplate = logTemplate;
    }

    @XmlElement(name="defaultLogLevel")
    public String getDefaultLogLevel() {
        return defaultLogLevel;
    }

    public void setDefaultLogLevel(String defaultLogLevel) {
        this.defaultLogLevel = defaultLogLevel;
    }




    @XmlElementWrapper(name="keyWords")
    @XmlElement(name = "keyWord")
    public List<String> getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(List<String> keyWord) {
        this.keyWord = keyWord;
    }

    public LogContext() {
    }
}
