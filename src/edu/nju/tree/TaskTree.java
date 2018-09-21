package edu.nju.tree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TaskTree {

    private TreeNode root;

    public TaskTree(String path) {
        root = new TreeNode(null,null);
        init(path);
    }

    private void init(String path){
        try {
            FileReader fileReader = new FileReader(new File(path));
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            TreeNode current = root;
            TreeNode child;

            String line = bufferedReader.readLine();

            while (line != null){

                String [] strArray = line.split("/");
                String taskId = strArray[0];
                String taskName = strArray[1];
                String prePatten = strArray[2];
                String postPatten = strArray[3];
                Task newTask = new Task(taskId,taskName,prePatten,postPatten);

                //null和1
                if(current.getContent() == null){
                    child = new TreeNode(current,newTask);
                    current.addChild(child);
                    System.out.println("1"+current.getContent()+" "+child.getContent());
                    current = child;
                    System.out.println("1"+current.getContent()+" "+child.getContent());
                    line = bufferedReader.readLine();
                    continue;
                }

                Task currentTask = (Task) current.getContent();
                String [] currentTaskIdArray = currentTask.getTaskId().split("\\.");
                String [] taskIdArray = taskId.split("\\.");

                //1和1.1
//                System.out.println("2 "+currentTask.getTaskId()+" "+taskId);
//                System.out.println("2 "+currentTaskIdArray.length+" "+taskIdArray.length);
//                System.out.println("2 "+currentTask.getTaskId()+" "+taskId.substring(0,taskId.lastIndexOf('.')));
                if(currentTaskIdArray.length+1 == taskIdArray.length && currentTask.getTaskId().equals(taskId.substring(0,taskId.lastIndexOf('.')))){
                    child = new TreeNode(current,newTask);
                    current.addChild(child);
                    System.out.println("2"+current.getContent()+" "+child.getContent());
                    current = child;
                    System.out.println("2"+current.getContent()+" "+child.getContent());
                    line = bufferedReader.readLine();
                    continue;
                }

                //1.1.1和2
                int backTime = currentTaskIdArray.length-taskIdArray.length+1;
                for (int i = 0; i < backTime; i++) {
                    current = current.getFatherNode();
                }

                child = new TreeNode(current,newTask);
                current.addChild(child);
                System.out.println("3"+current.getContent()+" "+child.getContent());
                current = child;
                System.out.println("3"+current.getContent()+" "+child.getContent());

                line = bufferedReader.readLine();
            }

            bufferedReader.close();
            fileReader.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void logSegmentation(String path){
        try {

            Stack<TreeNode> treeStack = new Stack<>();
            treeStack.add(root);
            while(!treeStack.isEmpty()){
                TreeNode current = treeStack.pop();
                if(current.getContent()!=null){

                    Task currentTask = (Task) current.getContent();
                    String prePatten = currentTask.getPrePatten();
                    String postPatten = currentTask.getPostPatten();
                    boolean hasPrePatten = false;
                    boolean hasPostPatten = false;
                    boolean isError = false;
                    List<String> logList = new ArrayList<>();

                    FileReader fileReader = new FileReader(new File(path));
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String line = bufferedReader.readLine();

                    while (line != null){
                        if(hasPrePatten){
                            if(line.indexOf("ERROR") != -1) isError = true;
                            logList.add(line);
                        }

                        if(line.indexOf(prePatten) != -1){
                            hasPrePatten = true;
                            logList.add(line);

                        }

                        if (line.indexOf(postPatten) != -1){
                            hasPostPatten = true;
                            logList.add(line);
                            break;
                        }

                        line = bufferedReader.readLine();
                    }
                    bufferedReader.close();
                    fileReader.close();

                    if (!hasPrePatten){
                        currentTask.setTaskStatus(TaskStatus.PRE_LOSS);
                    }else if(hasPrePatten&&!hasPostPatten){
                        currentTask.setTaskStatus(TaskStatus.POST_LOSS);
                    }else {
                        currentTask.setLogs(logList);
                        if (isError) currentTask.setTaskStatus(TaskStatus.ERROR);
                    }
                }


                List list = current.getChildrenList();
                if(!list.isEmpty()){
                    for (int i = 0; i < list.size(); i++) {
                        treeStack.push((TreeNode) list.get(i));
                    }
                }

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public JTree output(){
        JTree tree;

        Stack<TreeNode> treeStack = new Stack<>();
        treeStack.add(root);
        while(!treeStack.isEmpty()) {
            TreeNode current = treeStack.pop();
            if (current.getContent() == null){
                current.getShowNode().setUserObject("Log segmentation by task template");
            }

            if (current.getContent() != null) {
                Task task = (Task) current.getContent();
                current.getShowNode().setUserObject(task.getTaskId()+" "+task.getTaskName()+" "+task.getTaskStatus());
            }
            List list = current.getChildrenList();
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    treeStack.push((TreeNode) list.get(i));
                }
            }
        }

        tree = new JTree(root.getShowNode());
        return tree;


    }

    public void traverse(){
        root.traverse();
    }





}
