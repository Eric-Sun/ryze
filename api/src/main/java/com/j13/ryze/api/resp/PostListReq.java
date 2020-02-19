package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class PostListReq {
    @Parameter(desc = "")
    private int barId;
    @Parameter(desc = "")
    private int pageNum;
    @Parameter(desc = "")
    private int size;
    @Parameter(desc = "")
    private int type;
    @Parameter(desc = "")
    private String userToken;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBarId() {
        return barId;
    }

    public void setBarId(int barId) {
        this.barId = barId;
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
