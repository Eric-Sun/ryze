package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;

import java.util.List;

public class AdminBannerListResp {
    private List<AdminBannerDetailResp> bannerList = Lists.newLinkedList();

    public List<AdminBannerDetailResp> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<AdminBannerDetailResp> bannerList) {
        this.bannerList = bannerList;
    }
}

