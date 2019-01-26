package com.j13.garen.api.resp;

import com.j13.poppy.anno.Parameter;

public class UserRegisterResp {
    @Parameter(desc="用户id")
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
