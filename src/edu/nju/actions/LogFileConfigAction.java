package edu.nju.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import edu.nju.tree.TaskTree;

/**
 * 日志文件配置Action
 * 用于选择要进行分析的日志文件路径
 * 
 * @author NJU
 */
public class LogFileConfigAction extends AnAction {

    /** 选中的日志文件路径（静态变量，供其他类使用） */
    public static String logFilePath;

    /** 任务模板文件路径 */
    public final String taskTemplate = this.getClass().getResource("/TaskTemplate").getFile();

    /**
     * 执行Action逻辑
     * 打开文件选择器，让用户选择日志文件，并保存文件路径
     * 
     * @param anActionEvent Action事件
     */
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        // TODO: insert action logic here

        Project project = anActionEvent.getProject();
        // 创建文件选择器描述符：允许选择文件，不允许选择目录、JAR文件等
        FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(true,false,false,false,false,false);
        VirtualFile toSelect = FileChooser.chooseFile(fileChooserDescriptor,project,null);
        if (toSelect == null) return;
        // 保存选中的日志文件路径
        logFilePath = toSelect.getPath();



    }
}
