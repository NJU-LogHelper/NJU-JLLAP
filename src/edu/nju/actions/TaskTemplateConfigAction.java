package edu.nju.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import edu.nju.tree.TaskTree;

import java.io.*;

/**
 * 任务模板配置Action
 * 用于配置日志分割任务模板，允许用户编辑和保存任务模板规则
 * 
 * @author NJU
 */
public class TaskTemplateConfigAction extends AnAction {

    /** 任务模板文件路径 */
    public final String taskTemplate = this.getClass().getResource("/TaskTemplate").getFile();

    /**
     * 执行Action逻辑
     * 读取当前任务模板，显示编辑对话框供用户修改，然后保存新的模板内容
     * 
     * @param anActionEvent Action事件
     */
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        // TODO: insert action logic here
        Project project = anActionEvent.getProject();
        String initialTaskTemplate = "";
        
        // 读取现有的任务模板文件内容
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(taskTemplate));
            String line = bufferedReader.readLine();
            while (line != null){
                initialTaskTemplate = initialTaskTemplate + line+"\n";
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 显示多行输入对话框，让用户编辑任务模板
        String newTaskTemplate = Messages.showMultilineInputDialog(project,"Write the task template by rule","Task Template Config",initialTaskTemplate, Messages.getInformationIcon(), null);
        if(newTaskTemplate==null){
            return;
        }

        // 将用户编辑后的模板内容保存到文件
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(taskTemplate));
            fileOutputStream.write(newTaskTemplate.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
