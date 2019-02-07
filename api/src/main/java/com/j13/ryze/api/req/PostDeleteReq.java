package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class PostDeleteReq {
    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private int postId;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
