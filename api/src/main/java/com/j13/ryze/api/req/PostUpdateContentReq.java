package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class PostUpdateContentReq {
    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private int postId;
    @Parameter(desc = "")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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
