package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class PainterUpdateReq {
    @Parameter(desc="账号id")
    private int accountId;
    @Parameter(desc="简介")
    private String brief;
    @Parameter(desc="真实姓名")
    private String realName;
    @Parameter(desc="手机号")
    private String mobile;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
