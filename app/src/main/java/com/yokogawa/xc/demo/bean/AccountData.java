package com.yokogawa.xc.demo.bean;

public class AccountData {
    private boolean isLogin;
    private String userName;

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
