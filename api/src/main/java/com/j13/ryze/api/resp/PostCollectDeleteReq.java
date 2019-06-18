package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class PostCollectDeleteReq {
    @Parameter(desc = "")
    private int postId;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
