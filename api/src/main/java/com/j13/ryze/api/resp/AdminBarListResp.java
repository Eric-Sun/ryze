package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;

import java.util.List;

public class AdminBarListResp {

    private List<AdminBarDetailResp> data = Lists.newLinkedList();

    public List<AdminBarDetailResp> getData() {
        return data;
    }

    public void setData(List<AdminBarDetailResp> data) {
        this.data = data;
    }
}
