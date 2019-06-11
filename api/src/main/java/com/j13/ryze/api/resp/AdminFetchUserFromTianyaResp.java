package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class AdminFetchUserFromTianyaResp {
    @Parameter(desc = "")
    private int successCount;

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }
}
