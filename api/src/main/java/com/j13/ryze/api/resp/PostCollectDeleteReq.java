package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class PostCollectDeleteReq {
    @Parameter(desc = "")
    private int collectId;

    public int getCollectId() {
        return collectId;
    }

    public void setCollectId(int collectId) {
        this.collectId = collectId;
    }
}
