package com.j13.ryze.api.req;

public class AdminBannerPlanAddBannerReq {
    private int bannerPlanId;
    private int bannerId;

    public int getBannerPlanId() {
        return bannerPlanId;
    }

    public void setBannerPlanId(int bannerPlanId) {
        this.bannerPlanId = bannerPlanId;
    }

    public int getBannerId() {
        return bannerId;
    }

    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }
}
