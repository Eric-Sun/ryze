package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class AuthorityGetReq {
    @Parameter(desc="id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
