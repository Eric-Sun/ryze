package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class StarPostDeleteReq {
    @Parameter(desc = "")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
