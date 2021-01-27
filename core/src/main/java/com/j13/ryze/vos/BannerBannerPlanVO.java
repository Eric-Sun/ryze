package com.j13.ryze.vos;

public class BannerBannerPlanVO {
    private int id;
    private int bannerId;
    private int bannerPlanId;
    private long createtime;

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBannerId() {
        return bannerId;
    }

    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }

    public int getBannerPlanId() {
        return bannerPlanId;
    }

    public void setBannerPlanId(int bannerPlanId) {
        this.bannerPlanId = bannerPlanId;
    }
}
