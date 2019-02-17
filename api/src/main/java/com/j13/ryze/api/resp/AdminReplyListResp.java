package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;

import java.util.List;

public class AdminReplyListResp {
    private List<AdminReplyDetailResp> data  = Lists.newLinkedList();

    public List<AdminReplyDetailResp> getData() {
        return data;
    }

    public void setData(List<AdminReplyDetailResp> data) {
        this.data = data;
    }
}
