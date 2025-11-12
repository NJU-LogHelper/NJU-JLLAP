package edu.nju.actions;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * 日志分割工具窗口工厂类
 * 用于创建和初始化日志分割功能的工具窗口界面
 * 
 * @author NJU
 */
public class ToolWindowForLogSegmentation implements ToolWindowFactory{

    /** 主面板，用于显示日志分割结果 */
    private JPanel mainPanel;

    /**
     * 构造函数
     */
    public ToolWindowForLogSegmentation() {


    }

    /**
     * 创建工具窗口内容
     * 当工具窗口被打开时，会调用此方法来创建窗口内容
     * 
     * @param project 当前项目
     * @param toolWindow 工具窗口实例
     */
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        mainPanel = new JPanel();
        Content content = contentFactory.createContent(mainPanel,"", false);
        toolWindow.getContentManager().addContent(content);

    }
}
