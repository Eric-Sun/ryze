package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminReplyDeleteReq {
    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private int replyId;

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
