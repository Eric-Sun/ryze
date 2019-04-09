package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class NoticePostContentResp {
    @Parameter(desc = "")
    private int postId;
    @Parameter(desc = "")
    private int postUserId;
    @Parameter(desc = "")
    private String postUserNickName;
    @Parameter(desc = "")
    private String postUserAvatarImgUrl;
    @Parameter(desc = "")
    private String postTitle;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostUserAvatarImgUrl() {
        return postUserAvatarImgUrl;
    }

    public void setPostUserAvatarImgUrl(String postUserAvatarImgUrl) {
        this.postUserAvatarImgUrl = postUserAvatarImgUrl;
    }

    public int getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(int postUserId) {
        this.postUserId = postUserId;
    }

    public String getPostUserNickName() {
        return postUserNickName;
    }

    public void setPostUserNickName(String postUserNickName) {
        this.postUserNickName = postUserNickName;
    }
}
