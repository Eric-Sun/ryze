package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class AdminReplyListResp {
    private List<AdminReplyDetailResp> data = Lists.newLinkedList();

    @Parameter(desc = "")
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<AdminReplyDetailResp> getData() {
        return data;
    }

    public void setData(List<AdminReplyDetailResp> data) {
        this.data = data;
    }
}
