package edu.nju.actions;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ToolWindowForLogSegmentation implements ToolWindowFactory{

    private JPanel mainPanel;

    public ToolWindowForLogSegmentation() {


    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        mainPanel = new JPanel();
        Content content = contentFactory.createContent(mainPanel,"", false);
        toolWindow.getContentManager().addContent(content);

    }
}
