package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class UserMobilePasswordLoginReq {
    @Parameter(desc = "")
    private String mobile;
    @Parameter(desc = "")
    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
