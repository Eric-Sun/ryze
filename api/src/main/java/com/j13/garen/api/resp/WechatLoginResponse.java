package com.j13.garen.api.resp;

import com.j13.poppy.anno.Parameter;

public class WechatLoginResponse {
    @Parameter(desc = "")
    private int code = 0;
    @Parameter(desc = "")
    private String sessionKey;
    @Parameter(desc = "")
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
