package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class ResourceUpdateReq {
    @Parameter(desc="resource id")
    private int id;
    @Parameter(desc="resource name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
