package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminPostUpdateContentReq {
    @Parameter(desc = "")
    private int postId;
    @Parameter(desc = "")
    private String content;
    @Parameter(desc = "")
    private String title;
    @Parameter(desc = "")
    private int anonymous;
    @Parameter(desc = "")
    private int type;
    @Parameter(desc="")
    private String imgListStr;

    public String getImgListStr() {
        return imgListStr;
    }

    public void setImgListStr(String imgListStr) {
        this.imgListStr = imgListStr;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(int anonymous) {
        this.anonymous = anonymous;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

}
