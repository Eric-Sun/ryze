package com.j13.ryze.api.req;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class PostListResp {

    @Parameter(desc = "")
    private List<PostDetailResp> list = Lists.newLinkedList();

    public List<PostDetailResp> getList() {
        return list;
    }

    public void setList(List<PostDetailResp> list) {
        this.list = list;
    }
}
