package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminFetchUserCheckImgReq {
    @Parameter(desc = "")
    private int from;

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }
}
