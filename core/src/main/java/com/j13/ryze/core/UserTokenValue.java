package com.j13.ryze.core;

public class UserTokenValue {
    private String sessionKey;
    private String openId;
    private String t;

    public UserTokenValue() {
    }

    public UserTokenValue(String openId, String sessionKey, String t) {
        this.openId = openId;
        this.sessionKey = sessionKey;
        this.t = t;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }



}
