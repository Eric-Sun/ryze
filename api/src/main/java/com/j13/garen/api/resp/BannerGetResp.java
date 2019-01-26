package com.j13.garen.api.resp;

import com.j13.poppy.anno.Parameter;

public class BannerGetResp {

    @Parameter(desc = "banner's id")
    private int id;
    @Parameter(desc = "img url of the target banner")
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
