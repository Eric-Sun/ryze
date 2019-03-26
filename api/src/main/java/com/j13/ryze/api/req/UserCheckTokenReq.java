package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class UserCheckTokenReq {
    @Parameter(desc = "")
    private String t;

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }
}
