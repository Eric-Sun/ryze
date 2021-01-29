package com.j13.ryze.api.req;

public class AdminBannerPlanRemoveBannerReq {
    private int bannerId;
    private int bannerPlanId;

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
