package edu.nju.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> implements Serializable {

    private TreeNode fatherNode;

    private DefaultMutableTreeNode showNode;

    private List<TreeNode> childrenList;

    private T content;


    public TreeNode(TreeNode fatherNode, T content) {
        this.fatherNode = fatherNode;
        this.content = content;
        this.childrenList = new ArrayList<>();
        this.showNode = new DefaultMutableTreeNode();
    }

    public TreeNode getFatherNode() {
        return fatherNode;
    }

    public void setFatherNode(TreeNode fatherNode) {
        this.fatherNode = fatherNode;
    }

    public List<TreeNode> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<TreeNode> childrenList) {
        this.childrenList = childrenList;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }


    public DefaultMutableTreeNode getShowNode() {
        return showNode;
    }

    public void setShowNode(DefaultMutableTreeNode showNode) {
        this.showNode = showNode;
    }

    public void addChild(TreeNode child){
        this.childrenList.add(child);
        this.showNode.add(child.getShowNode());

    }

    public void traverse(){
        if(fatherNode != null){
            Task task = (Task)this.content;

//            System.out.println(task.getTaskId()+" "+task.getTaskName()+" "+task.getTaskStatus().getStr()+" "+task.getLogs().size());
//            System.out.println(this.getShowNode().getParent()+" "+this.getShowNode().getUserObject());

        }

        if (!this.childrenList.isEmpty()){
            int length = childrenList.size();
            for (int i = 0; i < length; i++) {
                TreeNode child = childrenList.get(i);
                child.traverse();
            }
        }
    }
}
