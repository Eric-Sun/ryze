package com.j13.garen.wx;

import com.alibaba.fastjson.JSON;
import com.j13.poppy.exceptions.CommonException;
import com.j13.garen.utils.InternetUtil;
import com.j13.garen.wx.resp.ErrorResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WechatMenuManager {
    private static Logger LOG = LoggerFactory.getLogger(WechatMenuManager.class);

    @Autowired
    WechatTokenManager wechatTokenManager;

    public void create(String data) {
        String token = wechatTokenManager.getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + token;
        LOG.info("request wechat. url={},data={}", url, data);
        String rawResponse = InternetUtil.post(url, data);
        ErrorResp resp = JSON.parseObject(rawResponse, ErrorResp.class);
        LOG.info("response wechat. {}", resp.toString());
        if (resp.getErrcode() != 0) {
            throw new CommonException(resp.getErrcode(), resp.getErrMsg());
        }
    }
}
