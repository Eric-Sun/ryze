package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class AccountLoginResp {
    @Parameter(desc = "")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
