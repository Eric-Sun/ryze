package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminReplyUpdateContentReq {
    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private int replyId;
    @Parameter(desc = "")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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
