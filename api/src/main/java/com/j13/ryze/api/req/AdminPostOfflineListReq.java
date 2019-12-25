package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminPostOfflineListReq {
    @Parameter(desc = "")
    private int barId;

    public int getBarId() {
        return barId;
    }

    public void setBarId(int barId) {
        this.barId = barId;
    }
}
