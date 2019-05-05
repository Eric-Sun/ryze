package com.j13.ryze.vos;

public class UserLockVO {
    private int id;
    private int userId;
    private String lockReason;
    private String unlockReason;
    private long locktime;
    private long unlocktime;
    private int lockType;
    private int unlockType;
    private int lockOperatorType;
    private int unlockOperatorType;

    public int getLockType() {
        return lockType;
    }

    public void setLockType(int lockType) {
        this.lockType = lockType;
    }

    public int getUnlockType() {
        return unlockType;
    }

    public void setUnlockType(int unlockType) {
        this.unlockType = unlockType;
    }

    public int getLockOperatorType() {
        return lockOperatorType;
    }

    public void setLockOperatorType(int lockOperatorType) {
        this.lockOperatorType = lockOperatorType;
    }

    public int getUnlockOperatorType() {
        return unlockOperatorType;
    }

    public void setUnlockOperatorType(int unlockOperatorType) {
        this.unlockOperatorType = unlockOperatorType;
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

    public String getLockReason() {
        return lockReason;
    }

    public void setLockReason(String lockReason) {
        this.lockReason = lockReason;
    }

    public String getUnlockReason() {
        return unlockReason;
    }

    public void setUnlockReason(String unlockReason) {
        this.unlockReason = unlockReason;
    }

    public long getLocktime() {
        return locktime;
    }

    public void setLocktime(long locktime) {
        this.locktime = locktime;
    }

    public long getUnlocktime() {
        return unlocktime;
    }

    public void setUnlocktime(long unlocktime) {
        this.unlocktime = unlocktime;
    }
}
