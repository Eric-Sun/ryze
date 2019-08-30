package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class MemberUnifiedOrderResp {
    @Parameter(desc = "")
    private String timestamp;
    @Parameter(desc = "")
    private String nonceStr;
    @Parameter(desc = "")
    private String packageStr;
    @Parameter(desc = "")
    private String paySign;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPackageStr() {
        return packageStr;
    }

    public void setPackageStr(String packageStr) {
        this.packageStr = packageStr;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }
}
