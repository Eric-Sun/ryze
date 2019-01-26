package com.j13.garen.api.resp;

import com.j13.poppy.anno.Parameter;

public class UserLoginResp {
    @Parameter(desc="用户id")
    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

}
