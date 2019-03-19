package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class WechatLoginResponse {
    @Parameter(desc = "")
    private String t;

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

}
