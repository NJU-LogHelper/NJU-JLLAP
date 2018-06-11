package edu.nju.ui.config.view;

import edu.nju.config.defaults.LogFrameworkDefaults;
import edu.nju.config.persist.service.LogHelperProjectService;

import javax.swing.*;

public class ConfigFrom {
    public JPanel mainPanel;
    public JComboBox<String> defaultFrameworkName;

    public ConfigFrom() {
        for(LogFrameworkDefaults logFrameworkDefaults: LogHelperProjectService.getInstance().getProjectConfiguration().getLogFrameworkDefaults()){
            defaultFrameworkName.addItem(logFrameworkDefaults.getName());
        }
        System.out.println(defaultFrameworkName.getItemAt(0));

    }
}
