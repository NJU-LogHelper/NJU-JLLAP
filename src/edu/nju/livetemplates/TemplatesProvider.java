package edu.nju.livetemplates;

import com.intellij.codeInsight.template.impl.*;

public class TemplatesProvider implements DefaultLiveTemplatesProvider
{
    private static final String[] TEMPLATES;
    
    public String[] getDefaultLiveTemplateFiles() {
        return TemplatesProvider.TEMPLATES.clone();
    }
    
    public String[] getHiddenLiveTemplateFiles() {
        return new String[0];
    }
    
    static {
        TEMPLATES = new String[] { "/liveTemplates/LogHelper" };
    }
}
