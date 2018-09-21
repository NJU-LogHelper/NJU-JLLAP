package edu.nju.tree;

public enum TaskStatus {

    PRE_LOSS(0,"pre patten loss"),POST_LOSS(1,"post patten loss"),ERROR(2,"task error"),NORMAL(3,"task normal");

    private int value;

    private String str;

    TaskStatus(int value, String str) {
        this.value = value;
        this.str = str;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
