package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;

import java.util.List;

public class ReplyListResp {
    private List<ReplyDetailResp> data  = Lists.newLinkedList();

    public List<ReplyDetailResp> getData() {
        return data;
    }

    public void setData(List<ReplyDetailResp> data) {
        this.data = data;
    }
}
