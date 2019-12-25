package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminStarPostAddReq {
    @Parameter(desc = "")
    private int postId;
    @Parameter(desc = "")
    private int value;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
