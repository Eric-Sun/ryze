package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class AdminTopicListResp {
    @Parameter(desc = "")
    private List<AdminTopicDetailResp> data = Lists.newLinkedList();

    public List<AdminTopicDetailResp> getData() {
        return data;
    }

    public void setData(List<AdminTopicDetailResp> data) {
        this.data = data;
    }
}
