package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;

import java.util.List;

public class ReplyListResp {
    private List<ReplyDetailResp> data  = Lists.newLinkedList();
    private int level1ReplySize ;

    public int getLevel1ReplySize() {
        return level1ReplySize;
    }

    public void setLevel1ReplySize(int level1ReplySize) {
        this.level1ReplySize = level1ReplySize;
    }

    public List<ReplyDetailResp> getData() {
        return data;
    }

    public void setData(List<ReplyDetailResp> data) {
        this.data = data;
    }
}
