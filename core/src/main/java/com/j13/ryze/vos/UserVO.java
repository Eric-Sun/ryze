package com.j13.ryze.vos;

public class UserVO {
    private int userId;
    private String nickName;
    private String anonNickName;
    private int avatarImgId;
    private String avatarUrl;
    private long createtime;
    private String anonLouUrl;
    private String anonXiaUrl;
    private int isLock;

    public int getIsLock() {
        return isLock;
    }

    public void setIsLock(int isLock) {
        this.isLock = isLock;
    }

    public String getAnonLouUrl() {
        return anonLouUrl;
    }

    public void setAnonLouUrl(String anonLouUrl) {
        this.anonLouUrl = anonLouUrl;
    }

    public String getAnonXiaUrl() {
        return anonXiaUrl;
    }

    public void setAnonXiaUrl(String anonXiaUrl) {
        this.anonXiaUrl = anonXiaUrl;
    }

    public String getAnonNickName() {
        return anonNickName;
    }

    public void setAnonNickName(String anonNickName) {
        this.anonNickName = anonNickName;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

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
