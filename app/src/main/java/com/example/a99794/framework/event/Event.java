package com.example.a99794.framework.event;

/**
 * Created by 99794 on 2018/4/13.
 */

public class Event {
    private String message;

    public Event(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
