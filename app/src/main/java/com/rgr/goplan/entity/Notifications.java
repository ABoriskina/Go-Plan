package com.rgr.goplan.entity;

public class Notifications {
    String fromWho;
    String toWho;
    String message;
    Events event;
    String isSeen;

    public String getFromWho() {
        return fromWho;
    }

    public void setFromWho(String fromWho) {
        this.fromWho = fromWho;
    }

    public String getToWho() {
        return toWho;
    }

    public void setToWho(String toWho) {
        this.toWho = toWho;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Events getEvent() {
        return event;
    }

    public void setEvent(Events event) {
        this.event = event;
    }

    public String isSeen() {
        return isSeen;
    }

    public void setSeen(String seen) {
        isSeen = seen;
    }

    public Notifications(String fromWho, String toWho, String message, Events event, String isSeen) {
        this.fromWho = fromWho;
        this.toWho = toWho;
        this.message = message;
        this.event = event;
        this.isSeen = isSeen;
    }
}
