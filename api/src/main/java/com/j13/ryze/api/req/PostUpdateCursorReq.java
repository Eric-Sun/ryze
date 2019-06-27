package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class PostUpdateCursorReq {
    @Parameter(desc = "")
    private int postId;
    @Parameter(desc = "")
    private int cursor;
    @Parameter(desc = "")
    private int pageNum;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getCursor() {
        return cursor;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }
}
