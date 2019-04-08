package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class ReplyDeleteReq {
    @Parameter(desc = "")
    private int replyId;

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }
}
