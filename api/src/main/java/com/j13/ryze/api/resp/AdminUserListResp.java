package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class AdminUserListResp {
    @Parameter(desc = "")
    private List<AdminUserDetailResp> list = Lists.newLinkedList();

    public List<AdminUserDetailResp> getList() {
        return list;
    }

    public void setList(List<AdminUserDetailResp> list) {
        this.list = list;
    }
}
