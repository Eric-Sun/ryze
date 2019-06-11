package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminFetchUserFromTianyaReq {
    @Parameter(desc = "")
    private int count;
    @Parameter(desc = "")
    private int from;

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
