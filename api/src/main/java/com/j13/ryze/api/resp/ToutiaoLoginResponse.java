package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class ToutiaoLoginResponse {
    @Parameter(desc = "")
    private String t;
    @Parameter(desc = "")
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

}
