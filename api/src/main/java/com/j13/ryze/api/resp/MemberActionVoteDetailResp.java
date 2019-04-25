package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class MemberActionVoteDetailResp {
    @Parameter(desc = "")
    private int id;
    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private String userName;
    @Parameter(desc = "")
    private String userAvatarUrl;
    @Parameter(desc = "")
    private int resourceId;
    @Parameter(desc = "")
    private int type;
    @Parameter(desc = "")
    private Object evidenceObject;
    @Parameter(desc = "")
    private long createtime;
    @Parameter(desc = "")
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getEvidenceObject() {
        return evidenceObject;
    }

    public void setEvidenceObject(Object evidenceObject) {
        this.evidenceObject = evidenceObject;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }
}
