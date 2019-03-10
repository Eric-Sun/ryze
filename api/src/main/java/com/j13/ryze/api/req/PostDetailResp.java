package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class PostDetailResp {
    @Parameter(desc = "")
    private int postId;
    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private String userName;
    @Parameter(desc = "")
    private String title;
    @Parameter(desc = "")
    private String content;
    @Parameter(desc = "")
    private int status;
    @Parameter(desc = "")
    private int anonymous;
    @Parameter(desc = "")
    private int type;
    @Parameter(desc = "")
    private String createtime;

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
