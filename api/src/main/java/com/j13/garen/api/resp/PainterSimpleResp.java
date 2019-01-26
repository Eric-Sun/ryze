package com.j13.garen.api.resp;

import com.j13.poppy.anno.Parameter;

public class PainterSimpleResp {
    @Parameter(desc="账号id")
    private int accountId;
    @Parameter(desc="姓名")
    private String realName;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
