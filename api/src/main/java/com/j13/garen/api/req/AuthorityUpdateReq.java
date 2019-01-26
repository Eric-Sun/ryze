package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class AuthorityUpdateReq {
    @Parameter(desc="id")
    private int id;
    @Parameter(desc="name")
    private String name;
    @Parameter(desc="resource id array. as ['1','2']")
    private String resourceIdArray;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResourceIdArray() {
        return resourceIdArray;
    }

    public void setResourceIdArray(String resourceIdArray) {
        this.resourceIdArray = resourceIdArray;
    }
}
