package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class MemberUnifiedOrderReq {
    @Parameter(desc = "")
    private int totalFee;

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }
}
