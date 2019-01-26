package com.j13.garen.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class OrderListResp {
    @Parameter(desc="order list")
    private List<OrderGetResp> list = Lists.newLinkedList();

    public List<OrderGetResp> getList() {
        return list;
    }

    public void setList(List<OrderGetResp> list) {
        this.list = list;
    }
}
