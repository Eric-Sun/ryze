package com.j13.ryze.api.req;

public class AdminBannerPlanListReq {
    private int pageNum;
    private int size;

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
