package com.j13.ryze.api.resp;

public class AdminBannerDetailResp {
    private int id;
    private String name;
    private int urlImgId;
    private long createtime;
    private int deleted;
    private ImgDetailResp imgDetailResp = new ImgDetailResp();

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

    public int getUrlImgId() {
        return urlImgId;
    }

    public void setUrlImgId(int urlImgId) {
        this.urlImgId = urlImgId;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public ImgDetailResp getImgDetailResp() {
        return imgDetailResp;
    }

    public void setImgDetailResp(ImgDetailResp imgDetailResp) {
        this.imgDetailResp = imgDetailResp;
    }
}

