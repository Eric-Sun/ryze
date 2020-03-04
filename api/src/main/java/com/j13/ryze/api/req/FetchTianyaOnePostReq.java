package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class FetchTianyaOnePostReq {
    @Parameter(desc = "")
    private int postId;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
