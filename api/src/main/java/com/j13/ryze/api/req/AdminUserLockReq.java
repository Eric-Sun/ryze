package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminUserLockReq {
    @Parameter(desc = "被封号的用户id")
    private int lockUserId;
    @Parameter(desc = "封号的原因类型")
    private int lockReasonType;
    @Parameter(desc = "封号的原因")
    private String lockReason;
    @Parameter(desc = "解封时间")
    private long unlockTime;

    public long getUnlockTime() {
        return unlockTime;
    }

    public void setUnlockTime(long unlockTime) {
        this.unlockTime = unlockTime;
    }

    public int getLockUserId() {
        return lockUserId;
    }

    public void setLockUserId(int lockUserId) {
        this.lockUserId = lockUserId;
    }

    public int getLockReasonType() {
        return lockReasonType;
    }

    public void setLockReasonType(int lockReasonType) {
        this.lockReasonType = lockReasonType;
    }

    public String getLockReason() {
        return lockReason;
    }

    public void setLockReason(String lockReason) {
        this.lockReason = lockReason;
    }
}
