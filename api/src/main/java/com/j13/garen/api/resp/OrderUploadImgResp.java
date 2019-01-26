package com.j13.garen.api.resp;

import com.j13.poppy.anno.Parameter;

public class OrderUploadImgResp {
    @Parameter(desc = "")
    private int imgId;

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
