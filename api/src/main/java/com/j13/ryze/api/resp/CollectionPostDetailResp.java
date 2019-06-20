package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class CollectionPostDetailResp {
    @Parameter(desc = "")
    private int collectionId;
    @Parameter(desc = "")
    private Object post;

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public Object getPost() {
        return post;
    }

    public void setPost(Object post) {
        this.post = post;
    }
}
