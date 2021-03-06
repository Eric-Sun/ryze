package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminReplyAddReq {
    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private int barId;
    @Parameter(desc = "")
    private int postId;
    @Parameter(desc = "")
    private String content;
    @Parameter(desc = "")
    private int anonymous;
    @Parameter(desc = "")
    private int lastReplyId;
    @Parameter(desc = "")
    private String imgListStr;

    public String getImgListStr() {
        return imgListStr;
    }

    public void setImgListStr(String imgListStr) {
        this.imgListStr = imgListStr;
    }

    public int getLastReplyId() {
        return lastReplyId;
    }

    public void setLastReplyId(int lastReplyId) {
        this.lastReplyId = lastReplyId;
    }

    public int getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(int anonymous) {
        this.anonymous = anonymous;
    }

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
