package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class UserMobileMessageCodeLoginReq {
    @Parameter(desc = "")
    private String mobile;
    @Parameter(desc = "")
    private String messageCode;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }
}
