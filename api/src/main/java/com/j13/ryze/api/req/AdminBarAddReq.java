package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminBarAddReq {
    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
