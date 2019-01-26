package com.j13.garen.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class ResourceListResp {
    @Parameter(desc="resource list")
    private List<ResourceGetResp> list = Lists.newLinkedList();

    public List<ResourceGetResp> getList() {
        return list;
    }

    public void setList(List<ResourceGetResp> list) {
        this.list = list;
    }
}
