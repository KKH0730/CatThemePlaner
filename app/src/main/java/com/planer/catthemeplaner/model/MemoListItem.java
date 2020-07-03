package com.planer.catthemeplaner.model;

public class MemoListItem {

    private int _id;
    private String memo;
    private String position;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private int alarm;
    private int importance;
    private String description;


    public MemoListItem(int _id, String memo, String position, String startDate, String endDate, String startTime, String endTime, int alarm, int importance, String description) {
        this._id = _id;
        this.memo = memo;
        this.position = position;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.alarm = alarm;
        this.importance = importance;
        this.description = description;
    }

    public MemoListItem(String memo, String startDate, String startTime) {
        this.memo = memo;
        this.startDate = startDate;
        this.startTime = startTime;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
