package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminPostQueryTitleReq {
    @Parameter(desc = "")
    private String name;
    @Parameter(desc = "")
    private int barId;
    @Parameter(desc = "")
    private int pageNum;
    @Parameter(desc = "")
    private int size;

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getBarId() {
        return barId;
    }

    public void setBarId(int barId) {
        this.barId = barId;
    }

}
