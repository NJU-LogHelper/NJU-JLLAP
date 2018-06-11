package edu.nju.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import java.io.*;

/**
 * Created by chentiange on 2018/5/7.
 */
public class CriticalInspectionConfigAction extends AnAction {
//    final String fileNameForClass = "E:\\workspace\\LogHelper\\resources\\criticalClass";
//    final String fileNameForMethod = "E:\\workspace\\LogHelper\\resources\\criticalMethod";
//    final String fileNameForMethod = "/Users/chentiange/Downloads/LogHelper/resources/criticalMethod";
//    final String fileNameForClass = "/Users/chentiange/Downloads/LogHelper/resources/criticalClass";
    final String fileNameForMethod = this.getClass().getResource("/criticalMethod").getFile();
    final String fileNameForClass = this.getClass().getResource("/criticalClass").getFile();
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        String initialValue = "";
        String line = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileNameForClass));
            line = bufferedReader.readLine();
            while (line != null){
                initialValue = initialValue + line+"\n";
                line = bufferedReader.readLine();
            }
            bufferedReader = new BufferedReader(new FileReader(fileNameForMethod));
            line = bufferedReader.readLine();
            while (line != null){
                initialValue = initialValue + line+"\n";
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        String newrules = Messages.showMultilineInputDialog(project,"Edit your config here","Critical rule config",initialValue, Messages.getInformationIcon(), null);
        /**
         * 修复空值bug
         */
        if(newrules==null){
            return;
        }

        String[] rules = newrules.split("\n");
        clearFile(fileNameForClass);
        clearFile(fileNameForMethod);

        for (String rule:rules){
            if (rule != null){
                rule += "\n";
                try {
                    //add new rule in file
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(fileNameForClass),true);
                    if (rule.contains(":")){
                        fileOutputStream = new FileOutputStream(new File(fileNameForMethod),true);
                    }
                    fileOutputStream.write(rule.getBytes());
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void clearFile(String fileName){
        File file =new File(fileName);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
