package com.j13.ryze.api.req;

public class AdminBannerAddReq {
    private String name;
    private int urlImgId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUrlImgId() {
        return urlImgId;
    }

    public void setUrlImgId(int urlImgId) {
        this.urlImgId = urlImgId;
    }
}
