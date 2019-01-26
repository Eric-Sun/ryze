package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class PainterAddReq {

    @Parameter(desc="账号登陆名")
    private String accountName;
    @Parameter(desc="登陆密码")
    private String password;
    @Parameter(desc="简介")
    private String brief;
    @Parameter(desc="真实姓名")
    private String realName;
    @Parameter(desc="手机号")
    private String mobile;
    @Parameter(desc="权限id，画师，管理员等")
    private int authorityId;

    public int getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(int authorityId) {
        this.authorityId = authorityId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
