package edu.nju.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import edu.nju.tree.TaskTree;

import javax.swing.*;
import java.awt.*;

/**
 * 日志分割Action
 * 执行日志文件的分割操作，并将分割结果以树形结构显示在工具窗口中
 * 
 * @author NJU
 */
public class LogSegmentationAction extends AnAction {

    /** 任务模板文件路径 */
    public final String taskTemplate = this.getClass().getResource("/TaskTemplate").getFile();

    /**
     * 执行Action逻辑
     * 根据任务模板对日志文件进行分割，并在工具窗口中显示分割结果树
     * 
     * @param anActionEvent Action事件
     */
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        // TODO: insert action logic here

        // 创建任务树并执行日志分割
        TaskTree taskTree = new TaskTree(taskTemplate);
        taskTree.logSegmentation(LogFileConfigAction.logFilePath);

        Project project = anActionEvent.getProject();
        if (project != null){
            // 获取日志分割工具窗口
            ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("ToolWindowForLogSegmentation");
            if( toolWindow != null){
                toolWindow.show(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
            }

            // 创建滚动面板和树形视图
            JScrollPane scrollPane = new JScrollPane();
            JTree tree = taskTree.output();
            scrollPane.setVisible(true);
//            scrollPane.setViewportView(taskTree.output());
            JPanel mainPane = (JPanel) toolWindow.getContentManager().getContent(0).getComponent();

            // 根据屏幕尺寸设置组件大小
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
