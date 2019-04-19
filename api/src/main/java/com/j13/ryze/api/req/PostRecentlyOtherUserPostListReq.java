package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class PostRecentlyOtherUserPostListReq {
    @Parameter(desc = "")
    private int otherUserId;
    @Parameter(desc = "")
    private int size;
    @Parameter(desc = "")
    private int pageNum;
    @Parameter(desc = "")
    private int barId;

    public int getBarId() {
        return barId;
    }

    public void setBarId(int barId) {
        this.barId = barId;
    }

    public int getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(int otherUserId) {
        this.otherUserId = otherUserId;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
