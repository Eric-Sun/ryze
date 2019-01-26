package com.j13.garen.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class BannerListResp {
    @Parameter(desc = "banner list")
    private List<BannerGetResp> list = Lists.newLinkedList();

    public List<BannerGetResp> getList() {
        return list;
    }

    public void setList(List<BannerGetResp> list) {
        this.list = list;
    }
}
