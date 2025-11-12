package edu.nju.ui.config.view;

import edu.nju.config.defaults.LogFrameworkDefaults;
import edu.nju.config.persist.service.LogHelperProjectService;

import javax.swing.*;

/**
 * 配置表单类。
 * 用于创建和管理配置界面的组件，特别是日志框架选择部分。
 */
public class ConfigFrom {
    /**
     * 主面板，通常是这个配置界面的根容器。
     */
    public JPanel mainPanel;
    
    /**
     * 下拉选择框，用于选择默认的日志框架名称。
     * 泛型 <String> 指定了下拉框中存储的项的类型。
     */
    public JComboBox<String> defaultFrameworkName;

    /**
     * 构造函数。
     * 在创建 ConfigFrom 实例时，负责初始化 UI 组件的数据。
     */
    public ConfigFrom() {
        // 1. 获取项目配置中预设的日志框架默认设置列表。
        //    - LogHelperProjectService.getInstance(): 获取服务单例。
        //    - getProjectConfiguration(): 获取项目配置对象。
        //    - getLogFrameworkDefaults(): 获取所有默认日志框架配置列表。
        for(LogFrameworkDefaults logFrameworkDefaults: LogHelperProjectService.getInstance().getProjectConfiguration().getLogFrameworkDefaults()){
            // 2. 遍历每一个日志框架默认设置，并将其名称添加到下拉选择框中。
            defaultFrameworkName.addItem(logFrameworkDefaults.getName());
        }
        
        // 3. 打印下拉选择框中的第一个项目（通常是默认选中的项）到控制台。
        System.out.println(defaultFrameworkName.getItemAt(0));

    }
}
