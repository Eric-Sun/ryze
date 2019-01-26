package com.j13.garen.wx.resp;

public class ErrorResp {
    private int errcode;
    private String errMsg;

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    @Override
    public String toString() {
        return "ErrorResp{" +
                "errcode=" + errcode +
                ", errMsg='" + errMsg + '\'' +
                '}';
    }
}
