package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

// 二级评论类
public class Level2ReplyDetailResp {
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
    @Parameter(desc = "回复的人的用户名")
    private String lastReplyUserName;
    @Parameter(desc = "")
    private String lastReplyAnonUserName;
    @Parameter(desc = "被回复人的用户id")
    private int lastReplyUserId;
    @Parameter(desc = "")
    private int barId;
    @Parameter(desc = "")
    private String userAvatarUrl;

    public void setLastReplyAnonUserName(String lastReplyAnonUserName) {
        this.lastReplyAnonUserName = lastReplyAnonUserName;
    }


    public int getLastReplyUserId() {
        return lastReplyUserId;
    }

    public void setLastReplyUserId(int lastReplyUserId) {
        this.lastReplyUserId = lastReplyUserId;
    }

    public String getLastReplyUserName() {
        return lastReplyUserName;
    }

    public void setLastReplyUserName(String lastReplyUserName) {
        this.lastReplyUserName = lastReplyUserName;
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
