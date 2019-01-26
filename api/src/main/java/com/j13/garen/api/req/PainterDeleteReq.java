package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class PainterDeleteReq {
    @Parameter(desc="账号Id")
    private int accountId;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
