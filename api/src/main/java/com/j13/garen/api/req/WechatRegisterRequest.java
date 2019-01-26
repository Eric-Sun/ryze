package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class WechatRegisterRequest {
    @Parameter(desc = "")
    private String code;
    @Parameter(desc = "")
    private String encryptedData;
    @Parameter(desc = "")
    private String iv;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}
