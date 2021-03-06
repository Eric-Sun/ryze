package com.j13.ryze.vos;

import com.google.common.collect.Lists;

import java.util.List;

public class ReplyVO implements Comparable<ReplyVO> {
    private int replyId;
    private int postId;
    private int userId;
    private String userName;
    private String userAvatarUrl;
    private int barId;
    private String content;
    private long createtime;
    private int anonymous;
    private int lastReplyId;
    private String lastReplyUserName = "";
    private int lastReplyUserId;
    private String lastReplyAnonUserName;
    private String imgListStr;
    private List<ImgVO> imgList = Lists.newLinkedList();
    private List<ReplyVO> replyList = Lists.newLinkedList();
    private int replySize;

    public int getReplySize() {
        return replySize;
    }

    public void setReplySize(int replySize) {
        this.replySize = replySize;
    }

    public List<ReplyVO> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<ReplyVO> replyList) {
        this.replyList = replyList;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public List<ImgVO> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImgVO> imgList) {
        this.imgList = imgList;
    }

    public String getImgListStr() {
        return imgListStr;
    }

    public void setImgListStr(String imgListStr) {
        this.imgListStr = imgListStr;
    }

    public String getLastReplyAnonUserName() {
        return lastReplyAnonUserName;
    }

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

    @Override
    public int compareTo(ReplyVO o) {
        return new Long(o.getCreatetime() - this.getCreatetime()).intValue();
    }
}
