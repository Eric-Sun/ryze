package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class ReplyListReq {
    @Parameter(desc = "")
    private int postId;
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

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
