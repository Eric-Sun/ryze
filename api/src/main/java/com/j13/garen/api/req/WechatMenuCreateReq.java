package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class WechatMenuCreateReq {
    @Parameter(desc="json data. see the wechat doc")
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
