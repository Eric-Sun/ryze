package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class TopicListResp {
    @Parameter(desc = "")
    private List<TopicDetailResp> data = Lists.newLinkedList();

    public List<TopicDetailResp> getData() {
        return data;
    }

    public void setData(List<TopicDetailResp> data) {
        this.data = data;
    }
}
