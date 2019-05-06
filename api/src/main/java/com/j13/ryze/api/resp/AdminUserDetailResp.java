package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class AdminUserDetailResp {
    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private String nickName;
    @Parameter(desc = "")
    private int sourceType;
    @Parameter(desc = "")
    private String anonNickName;
    @Parameter(desc = "")
    private int avatarImgId;
    @Parameter(desc = "")
    private String avatarUrl;
    @Parameter(desc = "")
    private long createtime;
    @Parameter(desc = "")
    private int isLock;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public String getAnonNickName() {
        return anonNickName;
    }

    public void setAnonNickName(String anonNickName) {
        this.anonNickName = anonNickName;
    }

    public int getAvatarImgId() {
        return avatarImgId;
    }

    public void setAvatarImgId(int avatarImgId) {
        this.avatarImgId = avatarImgId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public int getIsLock() {
        return isLock;
    }

    public void setIsLock(int isLock) {
        this.isLock = isLock;
    }
}
