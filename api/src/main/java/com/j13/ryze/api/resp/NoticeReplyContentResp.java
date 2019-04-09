package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class NoticeReplyContentResp {

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

    @Parameter(desc = "")
    private int targetReplyId;
    @Parameter(desc = "")
    private int targetReplyUserId;
    @Parameter(desc = "")
    private String targetReplyUserNickName;
    @Parameter(desc = "")
    private String targetReplyUserAvatarImgUrl;
    @Parameter(desc = "")
    private String targetReplyContent;
    @Parameter(desc = "")
    private int targetReplyRepliedUserId;
    @Parameter(desc = "")
    private String targetReplyRepliedUserAvatarImgUrl;
    @Parameter(desc = "")
    private String targetReplyRepliedUserNickName;


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

    public String getTargetReplyContent() {
        return targetReplyContent;
    }

    public void setTargetReplyContent(String targetReplyContent) {
        this.targetReplyContent = targetReplyContent;
    }

    public int getTargetReplyId() {
        return targetReplyId;
    }

    public void setTargetReplyId(int targetReplyId) {
        this.targetReplyId = targetReplyId;
    }

    public String getTargetReplyUserNickName() {
        return targetReplyUserNickName;
    }

    public void setTargetReplyUserNickName(String targetReplyUserNickName) {
        this.targetReplyUserNickName = targetReplyUserNickName;
    }

    public String getTargetReplyRepliedUserAvatarImgUrl() {
        return targetReplyRepliedUserAvatarImgUrl;
    }

    public void setTargetReplyRepliedUserAvatarImgUrl(String targetReplyRepliedUserAvatarImgUrl) {
        this.targetReplyRepliedUserAvatarImgUrl = targetReplyRepliedUserAvatarImgUrl;
    }

    public int getTargetReplyRepliedUserId() {
        return targetReplyRepliedUserId;
    }

    public void setTargetReplyRepliedUserId(int targetReplyRepliedUserId) {
        this.targetReplyRepliedUserId = targetReplyRepliedUserId;
    }

    public String getTargetReplyRepliedUserNickName() {
        return targetReplyRepliedUserNickName;
    }

    public void setTargetReplyRepliedUserNickName(String targetReplyRepliedUserNickName) {
        this.targetReplyRepliedUserNickName = targetReplyRepliedUserNickName;
    }

    public String getTargetReplyUserAvatarImgUrl() {
        return targetReplyUserAvatarImgUrl;
    }

    public void setTargetReplyUserAvatarImgUrl(String targetReplyUserAvatarImgUrl) {
        this.targetReplyUserAvatarImgUrl = targetReplyUserAvatarImgUrl;
    }

    public int getTargetReplyUserId() {
        return targetReplyUserId;
    }

    public void setTargetReplyUserId(int targetReplyUserId) {
        this.targetReplyUserId = targetReplyUserId;
    }
}
