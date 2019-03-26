package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class UserOtherUserInfoReq {
    @Parameter(desc = "")
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
