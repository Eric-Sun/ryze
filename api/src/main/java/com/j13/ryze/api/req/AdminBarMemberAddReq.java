package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminBarMemberAddReq {
    @Parameter(desc = "")
    private int barId;
    @Parameter(desc = "")
    private int userId;

    public int getBarId() {
        return barId;
    }

    public void setBarId(int barId) {
        this.barId = barId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
