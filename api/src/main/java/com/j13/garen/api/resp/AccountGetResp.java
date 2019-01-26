package com.j13.garen.api.resp;

import com.j13.poppy.anno.Parameter;

public class AccountGetResp {
    @Parameter(desc = "id")
    private int id;
    @Parameter(desc = "name")
    private String name;
    @Parameter(desc = "authorityId")
    private int authorityId;
    @Parameter(desc = "createtime")
    private long createtime;
    @Parameter(desc="brief")
    private String brief;
    @Parameter(desc="realNmae")
    private String realName;
    @Parameter(desc="mobile")
    private String mobile;
    @Parameter(desc="name of auth")
    private String authorityName;

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

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

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
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
