package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class AdminPostOfflineListResp {
    @Parameter(desc = "")
    private List<AdminPostDetailResp> data = Lists.newLinkedList();
    @Parameter(desc = "")
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<AdminPostDetailResp> getData() {
        return data;
    }

    public void setData(List<AdminPostDetailResp> data) {
        this.data = data;
    }
}
