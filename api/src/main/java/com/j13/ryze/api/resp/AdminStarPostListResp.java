package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class AdminStarPostListResp {
    @Parameter(desc = "")
    private List<AdminStarPostDetailResp> data = Lists.newLinkedList();

    public List<AdminStarPostDetailResp> getData() {
        return data;
    }

    public void setData(List<AdminStarPostDetailResp> data) {
        this.data = data;
    }
}
