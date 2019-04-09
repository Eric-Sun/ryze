package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class NoticeListResp {

    @Parameter(desc = "")
    private List<NoticeDetailResp> list = Lists.newLinkedList();

    public List<NoticeDetailResp> getList() {
        return list;
    }

    public void setList(List<NoticeDetailResp> list) {
        this.list = list;
    }
}
