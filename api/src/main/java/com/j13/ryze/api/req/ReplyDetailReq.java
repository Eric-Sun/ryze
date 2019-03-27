package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class ReplyDetailReq {
    @Parameter(desc = "")
    private int replyId;
    @Parameter(desc = "")
    private int size;
    @Parameter(desc = "")
    private int pageNum;

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

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }
}
