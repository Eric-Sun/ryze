package com.j13.ryze.api.resp;

public class AdminBannerUpdateReq {
    private int id;
    private int urlImgId;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
