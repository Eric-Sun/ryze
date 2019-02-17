package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class AdminBarQueryResp {
    @Parameter(desc = "")
    private List<AdminBarDetailResp> data = Lists.newLinkedList();

    public List<AdminBarDetailResp> getData() {
        return data;
    }

    public void setData(List<AdminBarDetailResp> data) {
        this.data = data;
    }
}
