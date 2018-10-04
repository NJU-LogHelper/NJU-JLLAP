package edu.nju.tree;

public enum TaskStatus {

    PRE_LOSS(0,"Pre patten loss"),POST_LOSS(1,"Post patten loss"),ERROR(2,"Task error"),NORMAL(3,"Task normal");

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
