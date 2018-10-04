package edu.nju.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Task implements Serializable{


    private String taskName;

    private String prePatten;

    private String postPatten;

    private List<String> logs;

    private TaskStatus taskStatus;


    public Task(String taskName, String prePatten, String postPatten) {

        this.taskName = taskName;
        this.prePatten = prePatten;
        this.postPatten = postPatten;
        this.logs = new ArrayList<>();
        this.taskStatus = TaskStatus.NORMAL;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getPrePatten() {
        return prePatten;
    }

    public void setPrePatten(String prePatten) {
        this.prePatten = prePatten;
    }

    public String getPostPatten() {
        return postPatten;
    }

    public void setPostPatten(String postPatten) {
        this.postPatten = postPatten;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }
}
