package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class ImgUploadResp {
    @Parameter(desc = "")
    private int imgId;

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
