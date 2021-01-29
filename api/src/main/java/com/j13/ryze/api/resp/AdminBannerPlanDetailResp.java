package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;

import java.util.List;

public class AdminBannerPlanDetailResp {
    private int id;
    private String name;
    private int type;
    private long createtime;
    private List<Integer> bannerIdList = Lists.newLinkedList();
    private List<AdminBannerDetailResp> bannerRespList = Lists.newLinkedList();

    public List<Integer> getBannerIdList() {
        return bannerIdList;
    }

    public void setBannerIdList(List<Integer> bannerIdList) {
        this.bannerIdList = bannerIdList;
    }

    public List<AdminBannerDetailResp> getBannerRespList() {
        return bannerRespList;
    }

    public void setBannerRespList(List<AdminBannerDetailResp> bannerRespList) {
        this.bannerRespList = bannerRespList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }
}
