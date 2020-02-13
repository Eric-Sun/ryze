package com.j13.ryze.api.req;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;
import com.j13.ryze.api.resp.AdminReplyDetailResp;
import com.j13.ryze.api.resp.PostCursorDetailResp;
import com.j13.ryze.api.resp.ImgDetailResp;

import java.util.List;

public class PostDetailResp {
    @Parameter(desc = "")
    private int postId;
    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private String userName;
    @Parameter(desc = "")
    private String title;
    @Parameter(desc = "如果字段超长的话会返回给前端截取后的内容")
    private String content;
    @Parameter(desc = "")
    private int status;
    @Parameter(desc = "")
    private int anonymous;
    @Parameter(desc = "")
    private int type;
    @Parameter(desc = "")
    private String createtime;
    @Parameter(desc = "")
    private String userAvatarUrl;
    @Parameter(desc = "")
    private int replyCount;
    @Parameter(desc = "")
    private List<ImgDetailResp> imgList = Lists.newLinkedList();
    @Parameter(desc = "是否已经被用户收藏，0为未收藏，1为已收藏")
    private int isCollection=0;
    @Parameter(desc = "内容是否已经超长了，如果超长的话，返回给前端的将会是截取过的内容，并且此字段设置为1")
    private int isContentLong = 0;
    @Parameter(desc = "一级评论的数量，用来算分页")
    private int level1ReplySize;
    @Parameter(desc="是否是加精，如果为1的话是加精帖子，0为不是")
    private int star=0;

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getLevel1ReplySize() {
        return level1ReplySize;
    }

    public void setLevel1ReplySize(int level1ReplySize) {
        this.level1ReplySize = level1ReplySize;
    }

    public int getIsContentLong() {
        return isContentLong;
    }

    public void setIsContentLong(int isContentLong) {
        this.isContentLong = isContentLong;
    }

    public int getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(int isCollection) {
        this.isCollection = isCollection;
    }

    public List<ImgDetailResp> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImgDetailResp> imgList) {
        this.imgList = imgList;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    private List<AdminReplyDetailResp> replies = Lists.newLinkedList();

    public List<AdminReplyDetailResp> getReplies() {
        return replies;
    }

    public void setReplies(List<AdminReplyDetailResp> replies) {
        this.replies = replies;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public int getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(int anonymous) {
        this.anonymous = anonymous;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
}
