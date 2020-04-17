package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminPostQueryReq {
    @Parameter(desc="")
    private int barId;
    @Parameter(desc = "")
    private int postId;
    @Parameter(desc = "")
    private String title;
    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private int pageNum;
    @Parameter(desc = "")
    private int size;

    public int getBarId() {
        return barId;
    }

    public void setBarId(int barId) {
        this.barId = barId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
