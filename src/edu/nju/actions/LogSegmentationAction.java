package edu.nju.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import edu.nju.tree.TaskTree;

import javax.swing.*;
import java.awt.*;

public class LogSegmentationAction extends AnAction {

    public final String taskTemplate = this.getClass().getResource("/TaskTemplate").getFile();

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        // TODO: insert action logic here

        TaskTree taskTree = new TaskTree(taskTemplate);
        taskTree.logSegmentation(LogFileConfigAction.logFilePath);

        Project project = anActionEvent.getProject();
        if (project != null){
            ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("ToolWindowForLogSegmentation");
            if( toolWindow != null){
                toolWindow.show(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
            }




            JScrollPane scrollPane = new JScrollPane();
            JTree tree = taskTree.output();
            scrollPane.setVisible(true);
//            scrollPane.setViewportView(taskTree.output());
            JPanel mainPane = (JPanel) toolWindow.getContentManager().getContent(0).getComponent();

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //得到屏幕的尺寸
            //设置主面板的大小
            mainPane.setPreferredSize(new Dimension((int)screenSize.getWidth()-50, (int) screenSize.getHeight()/3*2));
            //tree 设置大小
            tree.setPreferredSize(new Dimension((int)screenSize.getWidth()-50, (int) screenSize.getHeight()/3*2));
            scrollPane.setPreferredSize(new Dimension((int)screenSize.getWidth()-50, (int) screenSize.getHeight()/3*2-50));
            scrollPane.getVerticalScrollBar().setDoubleBuffered(true);
            scrollPane.setViewportView(tree);
            mainPane.add(scrollPane);


//            taskTree.traverse();
        }

    }



}
