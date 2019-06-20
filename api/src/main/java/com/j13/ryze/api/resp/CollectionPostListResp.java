package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class CollectionPostListResp {
    @Parameter(desc = "")
    private List<CollectionPostDetailResp> list = Lists.newLinkedList();

    public List<CollectionPostDetailResp> getList() {
        return list;
    }

    public void setList(List<CollectionPostDetailResp> list) {
        this.list = list;
    }
}
