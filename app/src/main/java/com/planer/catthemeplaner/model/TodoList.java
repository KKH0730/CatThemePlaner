package com.planer.catthemeplaner.model;

public class TodoList {

    private int _id;
    private String content;
    private int sharedNumber;
    private String rank;

    public TodoList(String content, int sharedNumber, String rank) {
        this.content = content;
        this.sharedNumber = sharedNumber;
        this.rank = rank;
    }

    public TodoList(int _id, String content, int sharedNumber, String rank) {
        this._id = _id;
        this.content = content;
        this.sharedNumber = sharedNumber;
        this.rank = rank;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSharedNumber() {
        return sharedNumber;
    }

    public void setSharedNumber(int sharedNumber) {
        this.sharedNumber = sharedNumber;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
