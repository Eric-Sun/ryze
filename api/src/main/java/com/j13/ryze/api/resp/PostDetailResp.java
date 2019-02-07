package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class PostDetailResp {
    @Parameter(desc = "")
    private int postId;
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

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
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
