package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class BarAddResp {
    @Parameter(desc = "")
    private int barId;

    public int getBarId() {
        return barId;
    }

    public void setBarId(int barId) {
        this.barId = barId;
    }
}
