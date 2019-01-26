package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class AuthorityAddReq {
    @Parameter(desc="name")
    private String name;
    @Parameter(desc="resource id array. as ['1','2']")
    private String resouceIdArray;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResouceIdArray() {
        return resouceIdArray;
    }

    public void setResouceIdArray(String resouceIdArray) {
        this.resouceIdArray = resouceIdArray;
    }
}
