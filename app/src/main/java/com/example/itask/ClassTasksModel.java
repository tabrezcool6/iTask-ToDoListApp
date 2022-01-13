package com.example.itask;

public class ClassTasksModel {

    private String task, date, userId;

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ClassTasksModel(){

    }

    public ClassTasksModel(String task, String date, String userId) {
        this.task = task;
        this.date = date;
        this.userId = userId;
    }

}
