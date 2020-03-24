package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class AdminPostDetailResp {

    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private String userName;
    @Parameter(desc = "")
    private String userAvatarUrl;
    @Parameter(desc = "")
    private int barId;
    @Parameter(desc = "")
    private String content;
    @Parameter(desc = "")
    private long createtime;
    @Parameter(desc = "")
    private int postId;
    @Parameter(desc = "")
    private long updatetime;
    @Parameter(desc = "")
    private String title;
    @Parameter(desc = "")
    private int status;
    @Parameter(desc = "")
    private int anonymous;
    @Parameter(desc = "")
    private int type;
    @Parameter(desc = "")
    List<ImgDetailResp> imgList = Lists.newLinkedList();
    private List<AdminLevelInfoResp> levelInfo = Lists.newLinkedList();
    @Parameter(desc = "是否是精华帖子，默认不是精华")
    private int star = 0;
    @Parameter(desc = "")
    private int replyCount = 0;
    @Parameter(desc="已经抓取的回复数量，用于后台显示")
    private int fReplyCount=0;

    public int getfReplyCount() {
        return fReplyCount;
    }

    public void setfReplyCount(int fReplyCount) {
        this.fReplyCount = fReplyCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}
