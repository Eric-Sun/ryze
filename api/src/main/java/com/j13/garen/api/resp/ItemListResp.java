package com.j13.garen.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class ItemListResp {

    @Parameter(desc="return item list.")
    private List<ItemGetResp> list = Lists.newLinkedList();

    public List<ItemGetResp> getList() {
        return list;
    }

    public void setList(List<ItemGetResp> list) {
        this.list = list;
    }
}
