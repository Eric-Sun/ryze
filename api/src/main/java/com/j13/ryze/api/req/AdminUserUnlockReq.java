package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminUserUnlockReq {
    @Parameter(desc = "解封用户id")
    private int unlockUserId;
    @Parameter(desc = "解封的原因")
    private String unlockReason;

    public int getUnlockUserId() {
        return unlockUserId;
    }

    public void setUnlockUserId(int unlockUserId) {
        this.unlockUserId = unlockUserId;
    }

    public String getUnlockReason() {
        return unlockReason;
    }

    public void setUnlockReason(String unlockReason) {
        this.unlockReason = unlockReason;
    }
}
