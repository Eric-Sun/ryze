package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AccountLoginReq {
    @Parameter()
    private String userName;
    @Parameter()
    private String userPassword;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
