package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminPostUndoDeleteReq {
    @Parameter(desc = "")
    private int postId;
    @Parameter(desc = "")
    private int barId;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getBarId() {
        return barId;
    }

    public void setBarId(int barId) {
        this.barId = barId;
    }
}
