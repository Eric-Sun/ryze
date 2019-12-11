package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class StarPostListResp {
    @Parameter(desc = "")
    private List<StarPostDetailResp> data = Lists.newLinkedList();

    public List<StarPostDetailResp> getData() {
        return data;
    }

    public void setData(List<StarPostDetailResp> data) {
        this.data = data;
    }
}
