package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class AccountUpdateReq {
    @Parameter(desc = "id")
    private int id;
    @Parameter(desc = "name")
    private String name;
    @Parameter(desc = "authorityId")
    private int authorityId;
    @Parameter(desc = "password after md5")
    private String passwordAfterMD5;
    @Parameter(desc="mobile")
    private String mobile;
    @Parameter(desc="")
    private String realName;
    @Parameter(desc="")
    private String brief;

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPasswordAfterMD5() {
        return passwordAfterMD5;
    }

    public void setPasswordAfterMD5(String passwordAfterMD5) {
        this.passwordAfterMD5 = passwordAfterMD5;
    }

    public int getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(int authorityId) {
        this.authorityId = authorityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
