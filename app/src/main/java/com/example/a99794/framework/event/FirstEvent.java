package com.example.a99794.framework.event;

/**
 * Created by 99794 on 2018/4/13.
 */

public class FirstEvent {
    private String name;
    private String pwd;

    public FirstEvent(String name, String pwd) {
        this.name = name;
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
