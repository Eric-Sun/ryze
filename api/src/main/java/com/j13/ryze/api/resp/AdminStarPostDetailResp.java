package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class AdminStarPostDetailResp {
    @Parameter(desc = "")
    private int id;
    @Parameter(desc = "")
    private int postId;
    @Parameter(desc = "")
    private String title;
    @Parameter(desc = "")
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
