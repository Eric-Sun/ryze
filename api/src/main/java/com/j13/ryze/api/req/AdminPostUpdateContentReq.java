package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminPostUpdateContentReq {
    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private int postId;
    @Parameter(desc = "")
    private String content;
    @Parameter(desc = "")
    private int barId;

    public int getBarId() {
        return barId;
    }

    public void setBarId(int barId) {
        this.barId = barId;
    }

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
