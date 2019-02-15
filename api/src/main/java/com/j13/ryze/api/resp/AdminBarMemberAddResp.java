package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class AdminBarMemberAddResp {
    @Parameter()
    private int barMemberId;

    public int getBarMemberId() {
        return barMemberId;
    }

    public void setBarMemberId(int barMemberId) {
        this.barMemberId = barMemberId;
    }
}
