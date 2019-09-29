package com.j13.ryze.services;

import com.alibaba.fastjson.JSON;
import com.j13.poppy.JedisManager;
import com.j13.poppy.RequestParams;
import com.j13.ryze.core.Constants;
import com.j13.ryze.core.Logger;
import com.j13.ryze.utils.CommonJedisManager;
import com.j13.ryze.utils.InternetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class WechatAPIService {


    @Autowired
    CommonJedisManager commonJedisManager;

    @PostConstruct
    public void init() {
        reloadAccessToken();
    }

    public String reloadAccessToken() {
        String raw = InternetUtil.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + Constants.Wechat.APPID + "&secret=" + Constants.Wechat.AppSecret);
        TokenResponse tokenResponse = JSON.parseObject(raw, TokenResponse.class);
        String accessToken = tokenResponse.getAccess_token();
        commonJedisManager.setAccessToken(accessToken, 3600);
        Logger.COMMON.info("reloaded token . token={}", accessToken);
        return accessToken;
    }

    public boolean msgCheck(String content) {
        // 检测token是否过期
        String accessToken = commonJedisManager.getAccessToken();
        if (accessToken == null) {
            accessToken = reloadAccessToken();
        }
        String raw = InternetUtil.post("https://api.weixin.qq.com/wxa/msg_sec_check?access_token="+accessToken,
                JSON.toJSONString(RequestParams.getInstance().add("content", content).end()));
        MsgCheckResponse response = JSON.parseObject(raw, MsgCheckResponse.class);
        if (response.getErrcode().equals("0"))
            return true;
        else {
            return false;
        }

    }

    static class MsgCheckResponse {
        private String errcode;
        private String errMsg;

        public String getErrcode() {
            return errcode;
        }

        public void setErrcode(String errcode) {
            this.errcode = errcode;
        }

        public String getErrMsg() {
            return errMsg;
        }

        public void setErrMsg(String errMsg) {
            this.errMsg = errMsg;
        }
    }

    static class TokenResponse {
        private String access_token;
        private String expires_in;
        private String errcode;
        private String errmsg;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(String expires_in) {
            this.expires_in = expires_in;
        }

        public String getErrcode() {
            return errcode;
        }

        public void setErrcode(String errcode) {
            this.errcode = errcode;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }
    }
}
