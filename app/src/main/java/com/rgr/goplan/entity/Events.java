package com.rgr.goplan.entity;

public class Events {

    private String userId;
    private String eventName;
    private String description;
    private String time;
    private String date;

    public Events(String userId, String eventName, String description, String time, String date) {
        this.userId = userId;
        this.eventName = eventName;
        this.description = description;
        this.time = time;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Events(String eventName, String description, String time, String date) {
        this.eventName = eventName;
        this.description = description;
        this.time = time;
        this.date = date;
    }
}
