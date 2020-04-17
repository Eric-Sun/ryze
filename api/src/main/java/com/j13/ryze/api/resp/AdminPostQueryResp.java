package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class AdminPostQueryResp {
    @Parameter(desc = "")
    private List<AdminPostDetailResp> list = Lists.newLinkedList();

    public List<AdminPostDetailResp> getList() {
        return list;
    }

    public void setList(List<AdminPostDetailResp> list) {
        this.list = list;
    }
}
