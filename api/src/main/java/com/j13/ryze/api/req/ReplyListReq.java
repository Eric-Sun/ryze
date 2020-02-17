package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class ReplyListReq {
    @Parameter(desc = "")
    private int postId;
    @Parameter(desc = "")
    private int pageNum = -1;
    @Parameter(desc = "")
    private String userToken;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

}
