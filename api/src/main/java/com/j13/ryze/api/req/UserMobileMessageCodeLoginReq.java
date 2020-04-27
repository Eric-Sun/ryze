package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class UserMobileMessageCodeLoginReq {
    @Parameter(desc = "")
    private String mobile;
    @Parameter(desc = "")
    private String messagecode;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMessagecode() {
        return messagecode;
    }

    public void setMessagecode(String messagecode) {
        this.messagecode = messagecode;
    }
}
