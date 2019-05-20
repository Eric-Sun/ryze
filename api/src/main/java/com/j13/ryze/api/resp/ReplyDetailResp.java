package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

// 一级评论类
public class ReplyDetailResp {
    @Parameter(desc = "")
    private int replyId;
    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private String userName;
    @Parameter(desc = "")
    private String userThumbUrl;
    @Parameter(desc = "")
    private String content;
    @Parameter(desc = "")
    private long createtime;
    @Parameter(desc = "")
    private int postId;
    @Parameter(desc = "")
    private int anonymous;
    @Parameter(desc = "")
    private int lastReplyId;
    @Parameter(desc = "")
    private int barId;
    @Parameter(desc = "二级评论的列表，包含三级评论")
    private List<Level2ReplyDetailResp> replyList = Lists.newLinkedList();
    @Parameter(desc = "")
    private String userAvatarUrl;
    @Parameter(desc = "回复该评论的总数量")
    private int replySize;
    @Parameter(desc = "")
    private List<ImgDetailResp> imgList= Lists.newLinkedList();

    public List<ImgDetailResp> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImgDetailResp> imgList) {
        this.imgList = imgList;
    }

    public int getReplySize() {
        return replySize;
    }

    public void setReplySize(int replySize) {
        this.replySize = replySize;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public int getBarId() {
        return barId;
    }

    public void setBarId(int barId) {
        this.barId = barId;
    }

    public int getLastReplyId() {
        return lastReplyId;
    }

    public void setLastReplyId(int lastReplyId) {
        this.lastReplyId = lastReplyId;
    }

    public List<Level2ReplyDetailResp> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Level2ReplyDetailResp> replyList) {
        this.replyList = replyList;
    }

    public int getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(int anonymous) {
        this.anonymous = anonymous;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserThumbUrl() {
        return userThumbUrl;
    }

    public void setUserThumbUrl(String userThumbUrl) {
        this.userThumbUrl = userThumbUrl;
    }
}
