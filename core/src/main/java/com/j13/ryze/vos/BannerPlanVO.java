package com.j13.ryze.vos;

import com.google.common.collect.Lists;

import java.util.List;

public class BannerPlanVO {
    private int id;
    private String name;
    private int type;
    private long createtime;
    private List<Integer> bannerIdList = Lists.newLinkedList();
    private List<BannerVO> bannerVOList = Lists.newLinkedList();

    public List<Integer> getBannerIdList() {
        return bannerIdList;
    }

    public void setBannerIdList(List<Integer> bannerIdList) {
        this.bannerIdList = bannerIdList;
    }

    public List<BannerVO> getBannerVOList() {
        return bannerVOList;
    }

    public void setBannerVOList(List<BannerVO> bannerVOList) {
        this.bannerVOList = bannerVOList;
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
