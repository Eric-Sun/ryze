package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class MemberActionPushVoteReq {
    @Parameter(desc = "")
    private int type;
    @Parameter(desc = "")
    private int resourceId;
    @Parameter(desc = "")
    private String reason;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
