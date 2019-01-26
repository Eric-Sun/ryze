package com.j13.garen.wx;

import com.alibaba.fastjson.JSON;
import com.j13.garen.core.Constants;
import com.j13.garen.utils.InternetUtil;
import com.j13.garen.wx.resp.AccessTokenResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WechatTokenManager {
    private static Logger LOG = LoggerFactory.getLogger(WechatTokenManager.class);
    private AccessToken accessToken = null;

    /**
     * get accesstoken from wx server
     *
     * @return
     */
    private AccessTokenResp requestAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" +
                Constants.Wechat.APPID + "&secret=" + Constants.Wechat.AppSecret;
        String rawResponse = InternetUtil.get(url);
        AccessTokenResp resp = JSON.parseObject(rawResponse, AccessTokenResp.class);
        return resp;
    }


    public String getAccessToken() {
        if (accessToken == null || accessToken.getTimestamp() < System.currentTimeMillis()) {
            LOG.info("access token expired.");
            accessToken = null;
            AccessTokenResp resp = requestAccessToken();
            accessToken = new AccessToken();
            accessToken.setTimestamp(new Long(resp.getExpires_in()));
            accessToken.setToken(resp.getAccess_token());
            LOG.info("request new token . {}", accessToken);
            return accessToken.getToken();

        } else {
            LOG.info("access token = {}", accessToken);
            return accessToken.getToken();
        }
    }

}

class AccessToken {
    private String token;
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "timestamp=" + timestamp +
                ", token='" + token + '\'' +
                '}';
    }
}
