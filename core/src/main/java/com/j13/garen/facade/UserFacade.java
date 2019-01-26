package com.j13.garen.facade;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.j13.garen.core.Constants;
import com.j13.garen.core.ErrorCode;
import com.j13.garen.daos.UserDAO;
import com.j13.garen.api.req.*;
import com.j13.garen.api.resp.*;
import com.j13.garen.daos.WechatInfoDAO;
import com.j13.garen.utils.InternetUtil;
import com.j13.garen.utils.WechatUtil;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.util.BeanUtils;
import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.exceptions.CommonException;
import com.j13.garen.vos.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: sunbo
 * Date: 14-3-19
 * Time: 下午4:21
 * To change this template use File | Settings | File Templates.
 */
@Component
public class UserFacade {
    private static Logger LOG = LoggerFactory.getLogger(UserFacade.class);

    @Autowired
    UserDAO userDAO;
    @Autowired
    WechatInfoDAO wechatInfoDAO;

    @Action(name = "user.login", desc = "用户登陆，密码需要md5，code=1001（密码错误）")
    public UserLoginResp login(CommandContext ctxt, UserLoginReq req) {
        UserLoginResp resp = new UserLoginResp();
        String mobile = req.getMobile();
        String passwordAfterMD5 = req.getPassword();
        UserVO vo = null;
        try {
            vo = userDAO.loginByMobile(mobile, passwordAfterMD5);
        } catch (EmptyResultDataAccessException e) {
            LOG.info("password error. mobile={},password={}", mobile, passwordAfterMD5);
            throw new CommonException(ErrorCode.User.PASSWORD_NOT_RIGHT);
        }

        BeanUtils.copyProperties(resp, vo);
        return resp;
    }


    /**
     * @return userId if registered successfully
     * -1 is mobile existed                               
     * -2 is nickName existed
     */
    @Action(name = "user.register", desc = "用户注册,code=1002(手机号已经注册过)")
    public UserRegisterResp register(CommandContext ctxt, UserRegisterReq req) {
        UserRegisterResp resp = new UserRegisterResp();
        String mobile = req.getMobile();
        String password = req.getPassword();
//        String nickName = req.getNickName();
        // check mobile exists
        if (userDAO.mobileExisted(mobile)) {
            LOG.info("mobile existed. mobile={}", mobile);
            throw new CommonException(ErrorCode.User.MOBILE_EXISTED);
        }


//        long id = userDAO.register(mobile, password);
//        resp.setId(id);
        return resp;
    }


    @Action(name = "user.checkMobile", desc = "检查注册的手机号是否已经注册过了")
    public CommonResultResp checkMobile(CommandContext ctxt, UserCheckMobileReq req) {
        CommonResultResp resp = new CommonResultResp();
        String mobile = req.getMobile();
        if (userDAO.mobileExisted(mobile)) {
            LOG.info("mobile existed. mobile={}", mobile);
            throw new CommonException(ErrorCode.User.MOBILE_EXISTED);
        }
        return resp;
    }


    @Action(name = "user.wechatLogin", desc = "通过微信客户端尝试登录，如果没有注册的话会通过加密数据进行注册")
    public WechatLoginResponse wechatLogin(CommandContext ctxt, WechatLoginRequest req) {
        String code = req.getCode();
        String rawResponse = InternetUtil.get("https://api.weixin.qq.com/sns/jscode2session?appid=" + Constants.APP_ID + "&secret=" + Constants.APP_SECRET
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
            userId = userDAO.registerFromWechat(data.getNickName(), data.getAvatarUrl());
            userDAO.registerUserInfoFromWechat(userId, data.getCity(), data.getCountry(), data.getProvince(), data.getGender(), data.getLanguage());
            wechatInfoDAO.insert(userId, openId, sessionKey);
        } else {
            userDAO.updateFromWechat(userId, data.getNickName(), data.getAvatarUrl());
            userDAO.updateInfoFromWechat(userId, data.getCity(), data.getCountry(), data.getProvince(), data.getGender(), data.getLanguage());
            wechatInfoDAO.updateSessionKey(openId, sessionKey);
        }

        WechatLoginResponse resp = new WechatLoginResponse();
        resp.setSessionKey(sessionKey);
        resp.setUserId(userId);
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

