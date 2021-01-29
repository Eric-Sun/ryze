package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;

import java.util.List;

public class AdminBannerPlanListResp {
    private List<AdminBannerPlanDetailResp> list = Lists.newLinkedList();

    public List<AdminBannerPlanDetailResp> getList() {
        return list;
    }

    public void setList(List<AdminBannerPlanDetailResp> list) {
        this.list = list;
    }
}
