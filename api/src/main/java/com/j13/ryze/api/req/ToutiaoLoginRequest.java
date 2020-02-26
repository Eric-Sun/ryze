package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class ToutiaoLoginRequest {
    @Parameter(desc = "")
    private String code;
    @Parameter(desc = "")
    private String encryptedData;
    @Parameter(desc = "")
    private String iv;
    @Parameter(desc = "")
    private String userToken;

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

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
