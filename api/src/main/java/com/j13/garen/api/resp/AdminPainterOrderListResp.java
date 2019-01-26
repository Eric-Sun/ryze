package com.j13.garen.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class AdminPainterOrderListResp {
    @Parameter(desc="order list")
    private List<AdminPainterOrderGetResp> list = Lists.newLinkedList();

    public List<AdminPainterOrderGetResp> getList() {
        return list;
    }

    public void setList(List<AdminPainterOrderGetResp> list) {
        this.list = list;
    }
}
