package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class ListTopicResp {
    @Parameter(desc = "")
    private List<TopicDetailResp> list = Lists.newLinkedList();

    public List<TopicDetailResp> getList() {
        return list;
    }

    public void setList(List<TopicDetailResp> list) {
        this.list = list;
    }
}
