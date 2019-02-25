package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminReplyDeleteReq {
    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private int replyId;
    @Parameter(desc = "")
    private int postId;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
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
