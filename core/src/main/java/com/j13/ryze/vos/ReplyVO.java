package com.j13.ryze.vos;

import com.google.common.collect.Lists;

import java.util.List;

public class ReplyVO {
    private int replyId;
    private int postId;
    private int userId;
    private String userName;
    private int barId;
    private String content;
    private long createtime;
    private int anonymous;
    private int lastReplyId;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
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
}
