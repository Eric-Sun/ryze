package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminReplyUpdateReq {
    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private int replyId;
    @Parameter(desc = "")
    private String content;
    @Parameter(desc = "")
    private int anonymous;

    public int getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(int anonymous) {
        this.anonymous = anonymous;
    }

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
