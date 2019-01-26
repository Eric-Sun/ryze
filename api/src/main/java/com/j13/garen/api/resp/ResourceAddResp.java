package com.j13.garen.api.resp;

import com.j13.poppy.anno.Parameter;

public class ResourceAddResp {

    @Parameter(desc="resource id")
    private int resourceId;

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
