package com.j13.garen.api.resp;

import com.j13.poppy.anno.Parameter;

public class AccountGetAccountIdByNameResp {
    @Parameter(desc="account id")
    private int accountId;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
