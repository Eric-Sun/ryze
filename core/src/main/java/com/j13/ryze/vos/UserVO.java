package com.j13.ryze.vos;

public class UserVO {
    private int userId;
    private String nickName;
    private int avatarImgId;
    private String avatarUrl;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getAvatarImgId() {
        return avatarImgId;
    }

    public void setAvatarImgId(int avatarImgId) {
        this.avatarImgId = avatarImgId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
