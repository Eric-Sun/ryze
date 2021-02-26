package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;

import java.util.List;

public class BannerGetInfoResp {
    private List<BannerGetResp> data = Lists.newLinkedList();

    public List<BannerGetResp> getData() {
        return data;
    }

    public void setData(List<BannerGetResp> data) {
        this.data = data;
    }
}
