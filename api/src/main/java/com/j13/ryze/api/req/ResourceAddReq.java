package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class ResourceAddReq {
    @Parameter(desc="resource name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
