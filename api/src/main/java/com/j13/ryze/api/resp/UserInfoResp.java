package com.j13.ryze.api.resp;

public class UserInfoResp {
    private String nickName;
    private String anonNickName;
    private String avatarUrl;
    private long createtime;
    private int avatarImgId;

    public int getAvatarImgId() {
        return avatarImgId;
    }

    public void setAvatarImgId(int avatarImgId) {
        this.avatarImgId = avatarImgId;
    }

    public String getAnonNickName() {
        return anonNickName;
    }

    public void setAnonNickName(String anonNickName) {
        this.anonNickName = anonNickName;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
