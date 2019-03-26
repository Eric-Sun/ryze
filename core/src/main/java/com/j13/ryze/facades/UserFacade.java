package com.j13.ryze.facades;

import com.alibaba.fastjson.JSON;
import com.j13.poppy.JedisManager;
import com.j13.poppy.TokenManager;
import com.j13.poppy.anno.Action;
import com.j13.poppy.anno.NeedToken;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.exceptions.CommonException;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.DefaultReq;
import com.j13.ryze.api.req.UserCheckTokenReq;
import com.j13.ryze.api.req.UserOtherUserInfoReq;
import com.j13.ryze.api.req.WechatLoginRequest;
import com.j13.ryze.api.resp.UserInfoResp;
import com.j13.ryze.api.resp.WechatLoginResponse;
import com.j13.ryze.core.Constants;
import com.j13.ryze.core.UserTokenValue;
import com.j13.ryze.daos.UserDAO;
import com.j13.ryze.daos.WechatInfoDAO;
import com.j13.ryze.services.ImgService;
import com.j13.ryze.services.UserService;
import com.j13.ryze.utils.InternetUtil;
import com.j13.ryze.utils.WechatUtil;
import com.j13.ryze.vos.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {
    private static Logger LOG = LoggerFactory.getLogger(UserFacade.class);

    @Autowired
    UserDAO userDAO;
    @Autowired
    WechatInfoDAO wechatInfoDAO;

    @Autowired
    UserService userService;
    @Autowired
    ImgService imgService;

    @Autowired
    TokenManager tokenManager;
    @Autowired
    JedisManager jedisManager;


    @Action(name = "user.wechatLogin", desc = "通过微信客户端尝试登录，如果没有注册的话会通过加密数据进行注册")
    public WechatLoginResponse wechatLogin(CommandContext ctxt, WechatLoginRequest req) {
        String code = req.getCode();
        String rawResponse = InternetUtil.get("https://api.weixin.qq.com/sns/jscode2session?appid=" +
                Constants.Wechat.APPID + "&secret=" + Constants.Wechat.AppSecret
                + "&js_code=" + code + "&grant_type=authorization_code");
        WechatLoginRawJSCode2Session wechatInterfaceResponse =
                JSON.parseObject(rawResponse, WechatLoginRawJSCode2Session.class);

        String openId = wechatInterfaceResponse.getOpenid();
        String sessionKey = wechatInterfaceResponse.getSession_key();
        // 获取基础信息
        String json = WechatUtil.getUserInfo(req.getEncryptedData(), sessionKey, req.getIv());
        EncryptedData data = JSON.parseObject(json, EncryptedData.class);

        // 检查这个openId有没有对应的记录
        int userId = wechatInfoDAO.getUserId(openId);
        if (userId == -1) {
            // 需要注册
            // 随机一个anonNickName
            String anonNickName = userService.randomAnonNickName();
            // 微信提供的头像存在imgDAO中
            int imgId = imgService.saveWechatAvatar(data.getAvatarUrl());
            userId = userDAO.register(data.getNickName(), anonNickName, imgId, Constants.USER_SOURCE_TYPE.WECHAT);
            userDAO.registerUserInfoFromWechat(userId, data.getCity(), data.getCountry(), data.getProvince(), data.getGender(), data.getLanguage());
            wechatInfoDAO.insert(userId, openId, sessionKey);
        } else {
            // 检测之前存的微信的头像有没有变化，没有的话就不更新了
            UserVO userVO = userDAO.getUser(userId);
            int imgId = userVO.getAvatarImgId();
            String oldAvatarUrl = imgService.getWechatUrlFromImgId(imgId);
            if (oldAvatarUrl.trim().equals(data.getAvatarUrl())) {
                userDAO.updateFromWechat(userId, data.getNickName());
            } else {
                // 不一样，需要替换图
                // 删除原图
                imgService.deleteOldWechatAvatar(imgId);
                int newImgId = imgService.saveWechatAvatar(data.getAvatarUrl());
                userDAO.updateFromWechat(userId, data.getNickName(), newImgId);
            }
            userDAO.updateInfoFromWechat(userId, data.getCity(), data.getCountry(), data.getProvince(), data.getGender(), data.getLanguage());
            wechatInfoDAO.updateSessionKey(openId, sessionKey);
        }
        // 清楚之前user的token信息
        String oldValueStr = jedisManager.get(userId + ":t");
        if (oldValueStr != null) {
            UserTokenValue oldValue = JSON.parseObject(oldValueStr, UserTokenValue.class);
            String oldT = oldValue.getT();
            jedisManager.delete(oldT);
            LOG.debug("clean old t. t={}", oldT);
        }
        String t = tokenManager.genT();
        // 设置token，可以查询到uid
        tokenManager.setTicket(t, userId);
        // 设置user:t，可以反查到token和其他信息
        UserTokenValue value = new UserTokenValue(openId, sessionKey, t);
        String valueStr = JSON.toJSONString(value);
        jedisManager.set(userId + ":t", valueStr);
        LOG.debug("add new t. t={},userId={},:t={}", new Object[]{t, userId, valueStr});


        WechatLoginResponse resp = new WechatLoginResponse();
        resp.setT(t);
        return resp;
    }


    @Action(name = "user.info")
    @NeedToken
    public UserInfoResp info(CommandContext ctxt, DefaultReq req) {
        UserInfoResp resp = new UserInfoResp();
        UserVO userVO = userService.getUserInfo(ctxt.getUid());
        BeanUtils.copyProperties(resp, userVO);
        return resp;
    }


    @Action(name = "user.checkToken")
    public CommonResultResp checkToken(CommandContext ctxt, UserCheckTokenReq req) {
        CommonResultResp resp = new CommonResultResp();
        String t = req.getT();
        int id = tokenManager.checkTicket(t);
        if (id == 0) {
            return CommonResultResp.failure();
        } else {
            return CommonResultResp.success();
        }
    }


    @Action(name = "user.otherUserInfo")
    @NeedToken
    public UserInfoResp otherUserInfo(CommandContext ctxt, UserOtherUserInfoReq req) {
        UserInfoResp resp = new UserInfoResp();
        UserVO userVO = userService.getUserInfo(req.getUserId());
        BeanUtils.copyProperties(resp, userVO);
        return resp;
    }

}

class WechatLoginRawJSCode2Session {
    private String openid;
    private String session_key;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }
}


class EncryptedData {
    private String avatarUrl;
    private String city;
    private String country;
    private int gender;
    private String language;
    private String nickName;
    private String openId;
    private String province;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
