package edu.nju.config.Form;

import edu.nju.config.persist.service.LogHelperProjectService;
import edu.nju.ui.config.view.ConfigFrom;
import org.jetbrains.annotations.*;
import javax.swing.*;
import com.intellij.openapi.options.*;

public class LogHelperProjectConfigurable implements SearchableConfigurable {
    private ConfigFrom configFrom=new ConfigFrom();
    private LogHelperProjectService logHelperProjectService=LogHelperProjectService.getInstance();

    public LogHelperProjectConfigurable() {
    }

    @NotNull
    public String getId() {
        return "LogHelperConfig";
    }
    @Nls
    @Override
    public String getDisplayName() {
        return this.getId();
    }
    @Nullable
    public String getHelpTopic() {
        return this.getId();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return this.configFrom.mainPanel;
    }

    @Override
    public boolean isModified() {
        assert this.logHelperProjectService.getState() != null;
        return !this.logHelperProjectService.getState().getDefaultFrameworkName().equals(this.configFrom.defaultFrameworkName.getSelectedItem());

    }

    @Override
    public void apply() {
        this.logHelperProjectService.getProjectConfiguration().setDefaultFrameworkName((String) this.configFrom.defaultFrameworkName.getSelectedItem());
    }

    @Override
    public void reset(){
        this.configFrom.defaultFrameworkName.setSelectedItem("log4j-1.2");
    }
}
