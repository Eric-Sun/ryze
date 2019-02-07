package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;

import java.util.List;

public class BarListResp {

    private List<BarDetailResp> data = Lists.newLinkedList();

    public List<BarDetailResp> getData() {
        return data;
    }

    public void setData(List<BarDetailResp> data) {
        this.data = data;
    }
}
