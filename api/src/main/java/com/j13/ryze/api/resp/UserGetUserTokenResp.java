package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class UserGetUserTokenResp {
    @Parameter(desc = "")
    private String userToken;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
