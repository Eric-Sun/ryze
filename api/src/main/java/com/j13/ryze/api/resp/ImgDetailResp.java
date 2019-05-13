package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class ImgDetailResp {
    @Parameter(desc = "")
    private int imgId;
    @Parameter(desc = "")
    private String url;

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
