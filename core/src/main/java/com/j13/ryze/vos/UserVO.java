package com.j13.ryze.vos;

/**
 * mNickName和mAvatarImgId字段无法被getter访问，是通过访问nickname和avatarImgId的getter方法进行处理的
 */
public class UserVO {
    private int userId;
    private String nickName;
    private String anonNickName;
    private String mNickName;
    private int mAvatarImgId;
    private int avatarImgId;
    private String avatarUrl;
    private long createtime;
    private String anonLouUrl;
    private String anonXiaUrl;
    private int isLock;
    private int sourceType;

    public void setmNickName(String mNickName) {
        this.mNickName = mNickName;
    }

    public void setmAvatarImgId(int mAvatarImgId) {
        this.mAvatarImgId = mAvatarImgId;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

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
        if (mAvatarImgId == 0)
            return avatarImgId;
        else
            return mAvatarImgId;
    }

    public void setAvatarImgId(int avatarImgId) {
        this.avatarImgId = avatarImgId;
    }

    public String getNickName() {
        if (mNickName.equals(""))
            return nickName;
        else
            return mNickName;
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
