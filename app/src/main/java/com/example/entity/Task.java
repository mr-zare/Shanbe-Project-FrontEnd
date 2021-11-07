package com.example.entity;

import com.google.gson.annotations.SerializedName;

public class Task {
    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String desc;

    @SerializedName("alarm_check")
    private String dateTime;

    @SerializedName("category")
    private String category;

    @SerializedName("push_notification")
    private String notification;

    private String callBackTime;
    private String taskToken;

    @SerializedName("user_token")
    private String userToken;


    private int id;

    public Task(String title, String desc, String dateTime, String category, String notification, String userToken) {
        this.title = title;
        this.desc = desc;
        this.dateTime = dateTime;
        this.category = category;
        this.notification = notification;
        this.userToken = userToken;
    }

    public Task(String title, String desc, String dateTime, String category, String notification, String callBackTime, String taskToken, String userToken, int id) {
        this.title = title;
        this.desc = desc;
        this.dateTime = dateTime;
        this.category = category;
        this.notification = notification;
        this.callBackTime = callBackTime;
        this.taskToken = taskToken;
        this.userToken = userToken;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getCallBackTime() {
        return callBackTime;
    }

    public void setCallBackTime(String callBackTime) {
        this.callBackTime = callBackTime;
    }

    public String getTaskToken() {
        return taskToken;
    }

    public void setTaskToken(String taskToken) {
        this.taskToken = taskToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
