package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class ResourceDeleteReq {
    @Parameter(desc="resource id")
    private int id;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
