package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class UserCheckMobileReq {
    @Parameter(desc="手机号")
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
