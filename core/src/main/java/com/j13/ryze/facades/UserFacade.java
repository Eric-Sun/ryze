package com.j13.ryze.facades;

import com.alibaba.fastjson.JSON;
import com.j13.poppy.JedisManager;
import com.j13.poppy.anno.Action;
import com.j13.poppy.anno.NeedToken;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.*;
import com.j13.ryze.api.resp.*;
import com.j13.ryze.cache.TokenCache;
import com.j13.ryze.core.Constants;
import com.j13.ryze.daos.UserDAO;
import com.j13.ryze.daos.ThirdPartInfoDAO;
import com.j13.ryze.services.ImgService;
import com.j13.ryze.services.UserService;
import com.j13.ryze.utils.BaiduUtil;
import com.j13.ryze.utils.InternetUtil;
import com.j13.ryze.utils.WechatUtil;
import com.j13.ryze.vos.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {
    private static Logger LOG = LoggerFactory.getLogger(UserFacade.class);

    @Autowired
    UserDAO userDAO;
    @Autowired
    ThirdPartInfoDAO thirdPartInfoDAO;

    @Autowired
    UserService userService;
    @Autowired
    ImgService imgService;
    @Autowired
    TokenCache tokenCache;


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
        int userId = thirdPartInfoDAO.getUserId(openId, Constants.USER_SOURCE_TYPE.WECHAT);
        LOG.info("userId={},openId={},sessionKey={}", new Object[]{userId, openId, sessionKey});
        if (userId == -1) {
            // 需要注册
            // 随机一个anonNickName
            String anonNickName = userService.randomAnonNickName();
            // 微信提供的头像存在imgDAO中
            int imgId = imgService.saveWechatAvatar(data.getAvatarUrl());
            userId = userDAO.register(data.getNickName(), anonNickName, imgId, Constants.USER_SOURCE_TYPE.WECHAT, "");
            userDAO.registerUserInfoFromWechat(userId, data.getCity(), data.getCountry(), data.getProvince(), data.getGender(), data.getLanguage());
            thirdPartInfoDAO.insert(userId, openId, sessionKey, Constants.USER_SOURCE_TYPE.WECHAT);
        } else {
            // 检测之前存的微信的头像有没有变化，没有的话就不更新了
            UserVO userVO = userDAO.getUser(userId);
            int imgId = userVO.getAvatarImgId();
            String oldAvatarUrl = imgService.getUrlFromImgId(imgId);
            if (oldAvatarUrl.trim().equals(data.getAvatarUrl())) {
                userDAO.update(userId, data.getNickName());
            } else {
                // 不一样，需要替换图
                // 删除原图
                // 10.17 不需要删除原图，直接update imgId就可以了
//                imgService.deleteOldWechatAvatar(imgId);
                int newImgId = imgService.saveWechatAvatar(data.getAvatarUrl());
                userDAO.update(userId, data.getNickName(), newImgId);
            }
            userDAO.updateInfoFromWechat(userId, data.getCity(), data.getCountry(), data.getProvince(), data.getGender(), data.getLanguage());
            thirdPartInfoDAO.updateSessionKey(openId, sessionKey, Constants.USER_SOURCE_TYPE.WECHAT);
        }
        String token = userService.resetTokenCache(userId);

        // 保存客户端的userToken
        if (req.getUserToken() != null) {
            userService.setUserToken(userId, req.getUserToken());
            LOG.info("update userToken. userId={},userToken={}", userId, req.getUserToken());
        } else {
            LOG.info("no userToken. app not set userToken. userId={}", userId);
        }

        WechatLoginResponse resp = new WechatLoginResponse();
        resp.setUserId(userId);
        resp.setT(token);
        return resp;
    }


    @Action(name = "user.toutiaoLogin", desc = "头条注册")
    public ToutiaoLoginResponse toutiaoLogin(CommandContext ctxt, ToutiaoLoginRequest req) {
        String code = req.getCode();
        String rawResponse = InternetUtil.get("https://developer.toutiao.com/api/apps/jscode2session?appid=" +
                Constants.TOUTIAO.APPID + "&secret=" + Constants.TOUTIAO.AppSecret
                + "&code=" + code);
        ToutiaoLoginRawJSCode2Session toutiaoInterfaceResponse =
                JSON.parseObject(rawResponse, ToutiaoLoginRawJSCode2Session.class);

        String openId = toutiaoInterfaceResponse.getOpenid();
        String sessionKey = toutiaoInterfaceResponse.getSession_key();
        // 获取基础信息
        String json = WechatUtil.getUserInfo(req.getEncryptedData(), sessionKey, req.getIv());
        EncryptedData data = JSON.parseObject(json, EncryptedData.class);

        // 检查这个openId有没有对应的记录
        int userId = thirdPartInfoDAO.getUserId(openId, Constants.USER_SOURCE_TYPE.TOUTIAO);
        LOG.info("userId={},openId={},sessionKey={}", new Object[]{userId, openId, sessionKey});
        if (userId == -1) {
            // 需要注册
            // 随机一个anonNickName
            String anonNickName = userService.randomAnonNickName();
            // 微信提供的头像存在imgDAO中
            int imgId = imgService.saveToutiaoAvatar(data.getAvatarUrl());
            userId = userDAO.register(data.getNickName(), anonNickName, imgId, Constants.USER_SOURCE_TYPE.TOUTIAO, "");
            userDAO.registerUserInfoFromWechat(userId, data.getCity(), data.getCountry(), data.getProvince(), data.getGender(), data.getLanguage());
            thirdPartInfoDAO.insert(userId, openId, sessionKey, Constants.USER_SOURCE_TYPE.TOUTIAO);
        } else {
            // 检测之前存的微信的头像有没有变化，没有的话就不更新了
            UserVO userVO = userDAO.getUser(userId);
            int imgId = userVO.getAvatarImgId();
            String oldAvatarUrl = imgService.getUrlFromImgId(imgId);
            if (oldAvatarUrl.trim().equals(data.getAvatarUrl())) {
                userDAO.update(userId, data.getNickName());
            } else {
                // 不一样，需要替换图
                // 删除原图
                // 10.17 不需要删除原图，直接update imgId就可以了
//                imgService.deleteOldWechatAvatar(imgId);
                int newImgId = imgService.saveToutiaoAvatar(data.getAvatarUrl());
                userDAO.update(userId, data.getNickName(), newImgId);
            }
            userDAO.updateInfoFromWechat(userId, data.getCity(), data.getCountry(), data.getProvince(), data.getGender(), data.getLanguage());
            thirdPartInfoDAO.updateSessionKey(openId, sessionKey, Constants.USER_SOURCE_TYPE.TOUTIAO);
        }
        String token = userService.resetTokenCache(userId);
        // 保存客户端的userToken
        if (req.getUserToken() != null) {
            userService.setUserToken(userId, req.getUserToken());
            LOG.info("update userToken. userId={},userToken={}", userId, req.getUserToken());
        } else {
            LOG.info("no userToken. app not set userToken. userId={}", userId);
        }

        ToutiaoLoginResponse resp = new ToutiaoLoginResponse();
        resp.setUserId(userId);
        resp.setT(token);
        return resp;
    }


    @Action(name = "user.baiduLogin", desc = "头条注册")
    public BaiduLoginResponse baiduLogin(CommandContext ctxt, BaiduLoginRequest req) {
        String code = req.getCode();
        String rawResponse = InternetUtil.get("https://spapi.baidu.com/oauth/jscode2sessionkey?client_id=" +
                Constants.BAIDU.APPID + "&sk=" + Constants.BAIDU.AppSecret
                + "&code=" + code);
        BaiduRawJSCode2Session baiduInterfaceResponse =
                JSON.parseObject(rawResponse, BaiduRawJSCode2Session.class);

        String openId = baiduInterfaceResponse.getOpenid();
        String sessionKey = baiduInterfaceResponse.getSession_key();
        // 获取基础信息
        String json = BaiduUtil.decrypt(req.getEncryptedData(), sessionKey);
        EncryptedData data = JSON.parseObject(json, EncryptedData.class);

        // 检查这个openId有没有对应的记录
        int userId = thirdPartInfoDAO.getUserId(openId, Constants.USER_SOURCE_TYPE.BAIDU);
        LOG.info("userId={},openId={},sessionKey={}", new Object[]{userId, openId, sessionKey});
        if (userId == -1) {
            // 需要注册
            // 随机一个anonNickName
            String anonNickName = userService.randomAnonNickName();
            // 微信提供的头像存在imgDAO中
            int imgId = imgService.saveBaiduAvatar(data.getAvatarUrl());
            userId = userDAO.register(data.getNickName(), anonNickName, imgId, Constants.USER_SOURCE_TYPE.BAIDU, "");
            userDAO.registerUserInfoFromWechat(userId, data.getCity(), data.getCountry(), data.getProvince(), data.getGender(), data.getLanguage());
            thirdPartInfoDAO.insert(userId, openId, sessionKey, Constants.USER_SOURCE_TYPE.BAIDU);
        } else {
            // 检测之前存的微信的头像有没有变化，没有的话就不更新了
            UserVO userVO = userDAO.getUser(userId);
            int imgId = userVO.getAvatarImgId();
            String oldAvatarUrl = imgService.getUrlFromImgId(imgId);
            if (oldAvatarUrl.trim().equals(data.getAvatarUrl())) {
                userDAO.update(userId, data.getNickName());
            } else {
                // 不一样，需要替换图
                // 删除原图
                // 10.17 不需要删除原图，直接update imgId就可以了
//                imgService.deleteOldWechatAvatar(imgId);
                int newImgId = imgService.saveBaiduAvatar(data.getAvatarUrl());
                userDAO.update(userId, data.getNickName(), newImgId);
            }
            userDAO.updateInfoFromWechat(userId, data.getCity(), data.getCountry(), data.getProvince(), data.getGender(), data.getLanguage());
            thirdPartInfoDAO.updateSessionKey(openId, sessionKey, Constants.USER_SOURCE_TYPE.BAIDU);
        }
        String token = userService.resetTokenCache(userId);

        // 保存客户端的userToken
        if (req.getUserToken() != null) {
            userService.setUserToken(userId, req.getUserToken());
            LOG.info("update userToken. userId={},userToken={}", userId, req.getUserToken());
        } else {
            LOG.info("no userToken. app not set userToken. userId={}", userId);
        }

        BaiduLoginResponse resp = new BaiduLoginResponse();
        resp.setUserId(userId);
        resp.setT(token);
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

    @Action(name = "user.getUserToken")
    public UserGetUserTokenResp getUserToken(CommandContext ctxt, UserGetUserTokenReq req) {

        String userToken = userService.getUserToken(ctxt.getUid());
        UserGetUserTokenResp resp = new UserGetUserTokenResp();
        resp.setUserToken(userToken);
        return resp;
    }


    @Action(name = "user.checkToken")
    public CommonResultResp checkToken(CommandContext ctxt, UserCheckTokenReq req) {
        CommonResultResp resp = new CommonResultResp();
        String t = req.getT();
        int id = tokenCache.getToken2UserId(t);
        LOG.info("checkToken. t={},id={}", t, id);
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


    @Action(name = "user.modifyNameAndAvatar")
    @NeedToken
    public CommonResultResp modifyNameAndAvatar(CommandContext ctxt, UserModifyNameAndAvatarReq req) {


        userService.modifyNameAndAvatar(ctxt.getUid(), req.getNewName(), req.getNewImgId());


        return CommonResultResp.success();
    }


    @Action(name = "user.mobilePasswordLogin", desc = "暂时不开发这个接口，主推手机验证码登陆")
    public UserMobilePasswordLoginResp mobilePasswordLogin(CommandContext ctxt, UserMobilePasswordLoginReq req) {
        UserMobilePasswordLoginResp resp = new UserMobilePasswordLoginResp();
        return resp;
    }


    @Action(name = "user.mobileMessageCodeLogin", desc = "首页注册登陆一体接口，如果没有注册就开始注册操作，然后登陆")
    public UserMobileMessageCodeLoginResp mobileMessageCodeLogin(CommandContext ctxt, UserMobileMessageCodeLoginReq req) {
        String mobile = req.getMobile();
        String messageCode = req.getMessageCode();
        UserMobileMessageCodeLoginResp resp = userService.mobileMessageCodeLogin(mobile, messageCode);
        return resp;
    }


    @Action(name = "user.sendMessageCode", desc = "")
    public CommonResultResp sendMessageCode(CommandContext ctxt, UserSendMessageCodeReq req) {
        userService.sendMessageCode(req.getMobile());
        return CommonResultResp.success();
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

class ToutiaoLoginRawJSCode2Session {
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

class BaiduRawJSCode2Session {
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
