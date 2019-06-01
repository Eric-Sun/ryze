package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminFetchUserFromQSBKReq {
    @Parameter(desc = "")
    private String token;
    @Parameter(desc="")
    private int pageNum;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
