package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class ResourceGetReq {
    @Parameter(desc="get a resource")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
