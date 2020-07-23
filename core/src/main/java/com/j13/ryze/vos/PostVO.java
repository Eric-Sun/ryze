package com.j13.ryze.vos;

import com.google.common.collect.Lists;

import java.util.List;

public class PostVO {
    private int postId;
    private int userId;
    private String userName;
    private String userAvatarUrl;
    private int barId;
    private String content;
    private long createtime;
    private int replyCount;
    private long updatetime;
    private String title;
    private int status;
    private int anonymous;
    private int type;
    private String imgListStr;
    private List<ImgVO> imgVOList = Lists.newLinkedList();
    private int star;
    private List<TopicVO> topicList = Lists.newLinkedList();
    private int auditStatus=0;

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public List<TopicVO> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<TopicVO> topicList) {
        this.topicList = topicList;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<ImgVO> getImgVOList() {
        return imgVOList;
    }

    public void setImgVOList(List<ImgVO> imgVOList) {
        this.imgVOList = imgVOList;
    }

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(long updatetime) {
        this.updatetime = updatetime;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostVO)) return false;

        PostVO postVO = (PostVO) o;

        if (postId != postVO.postId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return postId;
    }
}
