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
            TreeNode father = root;
            TreeNode child;

            String line = bufferedReader.readLine();

            while (line != null){

                if (line.indexOf("tasks:")!=-1){
                    father = (TreeNode) father.getChildrenList().get(father.getChildrenList().size()-1);
                    line = bufferedReader.readLine();
                }

                if(line.indexOf("name:")!=-1){
                    String nextLine = bufferedReader.readLine();
                    String nextNextLine = bufferedReader.readLine();
                    Task newTask = new Task(line.split("name:")[1],nextLine.split("pre:")[1],nextNextLine.split("post:")[1]);
                    child = new TreeNode(father,newTask);
                    father.addChild(child);
                }
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
                    }

                    if (isError) currentTask.setTaskStatus(TaskStatus.ERROR);
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
                current.getShowNode().setUserObject(task.getTaskName()+"---"+task.getTaskStatus().getStr());
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
