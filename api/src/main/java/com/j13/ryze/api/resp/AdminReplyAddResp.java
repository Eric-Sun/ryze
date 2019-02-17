package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class AdminReplyAddResp {
    @Parameter(desc = "")
    private int replyId;

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }
}
