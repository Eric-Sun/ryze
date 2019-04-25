package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class MemberActionVotePostOfflineEvidenceResp {
    @Parameter(desc = "")
    private int postId;
    @Parameter(desc = "")
    private String title;
    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private String userName;
    @Parameter(desc = "")
    private String userAvatarUrl;
    @Parameter(desc = "")
    private String breifContent;
    @Parameter(desc = "")
    private long createtime;
    @Parameter(desc = "")
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBreifContent() {
        return breifContent;
    }

    public void setBreifContent(String breifContent) {
        this.breifContent = breifContent;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }
}
