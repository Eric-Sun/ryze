package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class AdminPostDetailResp {

    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private int barId;
    @Parameter(desc = "")
    private String content;
    @Parameter(desc = "")
    private long createtime;
    @Parameter(desc = "")
    private int postId;
    @Parameter(desc = "")
    private String userName;
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
