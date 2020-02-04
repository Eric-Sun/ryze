package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class AdminPostDeletedListResp {
    private List<AdminPostDetailResp> list = Lists.newLinkedList();
    @Parameter(desc = "")
    private int count = 0;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<AdminPostDetailResp> getList() {
        return list;
    }

    public void setList(List<AdminPostDetailResp> list) {
        this.list = list;
    }
}
