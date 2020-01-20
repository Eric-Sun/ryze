package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class AdminPostListResp {
    @Parameter(desc = "")
    private List<AdminPostDetailResp> list = Lists.newLinkedList();
    @Parameter(desc = "")
    private String barName;
    @Parameter(desc="帖子总数")
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getBarName() {
        return barName;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }

    public List<AdminPostDetailResp> getList() {
        return list;
    }

    public void setList(List<AdminPostDetailResp> list) {
        this.list = list;
    }
}
