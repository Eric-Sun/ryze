package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminUserTxtLoadReq {
    @Parameter(desc = "")
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
