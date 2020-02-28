package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class AdminReplyDetailResp {
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
    // replyList 只有在调用detail类接口的时候才会有数据
    private List<AdminReplyDetailResp> replyList = Lists.newLinkedList();
    @Parameter(desc = "")
    private int replyListSize;
    @Parameter(desc = "")
    private String userAvatarUrl;
    @Parameter(desc = "")
    private int postUserId;
    @Parameter(desc = "")
    private List<AdminLevelInfoResp> levelInfo = Lists.newLinkedList();
    @Parameter(desc = "")
    private List<ImgDetailResp> imgList = Lists.newLinkedList();

    public int getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(int postUserId) {
        this.postUserId = postUserId;
    }

    public List<AdminReplyDetailResp> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<AdminReplyDetailResp> replyList) {
        this.replyList = replyList;
    }

    public int getReplyListSize() {
        return replyListSize;
    }

    public void setReplyListSize(int replyListSize) {
        this.replyListSize = replyListSize;
    }

    public List<ImgDetailResp> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImgDetailResp> imgList) {
        this.imgList = imgList;
    }

    public List<AdminLevelInfoResp> getLevelInfo() {
        return levelInfo;
    }

    public void setLevelInfo(List<AdminLevelInfoResp> levelInfo) {
        this.levelInfo = levelInfo;
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
