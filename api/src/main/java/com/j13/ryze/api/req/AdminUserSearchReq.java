package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminUserSearchReq {
    @Parameter(desc = "模糊匹配的用户名内容")
    private String text;
    @Parameter(desc = "")
    private int pageNum;
    @Parameter(desc = "")
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
