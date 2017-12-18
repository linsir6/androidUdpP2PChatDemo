package com.qf58.androidchatdemo;

/**
 * Created by linSir
 * date at 2017/12/17.
 * describe:
 */

public class User {

    private String username;
    private String pwd;

    public User(String username, String pwd) {
        this.username = username;
        this.pwd = pwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}