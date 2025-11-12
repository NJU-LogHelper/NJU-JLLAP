package edu.nju.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import java.io.*;

/**
 * 关键检查配置Action
 * 用于配置关键类和关键方法的检查规则
 * 规则中包含冒号(:)的将被保存到关键方法文件，否则保存到关键类文件
 * 
 * @author chentiange
 * @date 2018/5/7
 */
public class CriticalInspectionConfigAction extends AnAction {
    /** 关键方法配置文件路径 */
    final String fileNameForMethod = this.getClass().getResource("/criticalMethod").getFile();
    /** 关键类配置文件路径 */
    final String fileNameForClass = this.getClass().getResource("/criticalClass").getFile();
    
    /**
     * 执行Action逻辑
     * 读取现有的关键类和关键方法配置，显示编辑对话框供用户修改，然后保存新的配置
     * 
     * @param anActionEvent Action事件
     */
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        String initialValue = "";
        String line = "";
        
        // 读取关键类和关键方法的现有配置
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

        // 显示多行输入对话框，让用户编辑配置规则
        String newrules = Messages.showMultilineInputDialog(project,"Edit your config here","Critical rule config",initialValue, Messages.getInformationIcon(), null);
        /**
         * 修复空值bug
         */
        if(newrules==null){
            return;
        }

        // 清空原有配置文件
        String[] rules = newrules.split("\n");
        clearFile(fileNameForClass);
        clearFile(fileNameForMethod);

        // 根据规则内容分类保存：包含冒号的保存到方法文件，否则保存到类文件
        for (String rule:rules){
            if (rule != null){
                rule += "\n";
                try {
                    //add new rule in file
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(fileNameForClass),true);
                    // 如果规则包含冒号，说明是方法规则，保存到方法文件
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

    /**
     * 清空指定文件的内容
     * 如果文件不存在则创建新文件
     * 
     * @param fileName 要清空的文件路径
     */
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
