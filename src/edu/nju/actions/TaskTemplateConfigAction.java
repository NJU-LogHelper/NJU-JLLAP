package edu.nju.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import edu.nju.tree.TaskTree;

import java.io.*;

public class TaskTemplateConfigAction extends AnAction {


    public final String taskTemplate = this.getClass().getResource("/TaskTemplate").getFile();

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        // TODO: insert action logic here
        Project project = anActionEvent.getProject();
        String initialTaskTemplate = "";
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


        String newTaskTemplate = Messages.showMultilineInputDialog(project,"Write the task template by rule","Task Template Config",initialTaskTemplate, Messages.getInformationIcon(), null);
        if(newTaskTemplate==null){
            return;
        }

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
