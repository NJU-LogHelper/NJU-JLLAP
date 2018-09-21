package edu.nju.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import edu.nju.tree.TaskTree;

public class LogFileConfigAction extends AnAction {

    public static String logFilePath;

    public final String taskTemplate = this.getClass().getResource("/TaskTemplate").getFile();

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        // TODO: insert action logic here

        Project project = anActionEvent.getProject();
        FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(true,false,false,false,false,false);
        VirtualFile toSelect = FileChooser.chooseFile(fileChooserDescriptor,project,null);
        if (toSelect == null) return;
        logFilePath = toSelect.getPath();



    }
}
