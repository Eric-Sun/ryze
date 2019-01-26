package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class AccountCheckExistedReq {
    @Parameter(desc = "account name")
    private String name;
    @Parameter(desc = "account password .after md5")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
