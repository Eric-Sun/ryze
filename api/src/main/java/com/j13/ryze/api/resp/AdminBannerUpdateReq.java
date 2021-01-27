package com.j13.ryze.api.resp;

public class AdminBannerUpdateReq {
    private int bannerId;
    private int urlImgId;
    private String name;

    public int getBannerId() {
        return bannerId;
    }

    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }

    public int getUrlImgId() {
        return urlImgId;
    }

    public void setUrlImgId(int urlImgId) {
        this.urlImgId = urlImgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
