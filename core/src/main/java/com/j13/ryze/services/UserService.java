package com.j13.ryze.services;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.j13.poppy.exceptions.CommonException;
import com.j13.ryze.api.req.PostDetailResp;
import com.j13.ryze.api.resp.Level2ReplyDetailResp;
import com.j13.ryze.api.resp.ReplyDetailResp;
import com.j13.ryze.api.resp.UserMobileMessageCodeLoginResp;
import com.j13.ryze.cache.MessageCodeCache;
import com.j13.ryze.cache.TokenCache;
import com.j13.ryze.core.Constants;
import com.j13.ryze.core.ErrorCode;
import com.j13.ryze.core.Logger;
import com.j13.ryze.daos.*;
import com.j13.ryze.facades.UserTokenValue;
import com.j13.ryze.utils.CommonJedisManager;
import com.j13.ryze.utils.DateUtil;
import com.j13.ryze.vos.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;

/**
 * 封号逻辑：
 * 封号的字段为user表中的is_lock字段，如果这个字段是封号状态，则会在user_lock表中存在一条deleted=0的记录（可能会有其他的deleted=1的之前的封号但是已经解封的记录）
 * 当解除封号的时候，会把user_lock中的记录deleted=1，并且user表中的is_locK改为未封号状态
 */
@Service
public class UserService {
    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(UserService.class);


    @Autowired
    UserDAO userDAO;
    @Autowired
    OSSClientService ossClientService;
    @Autowired
    ImgDAO imgDAO;
    Random random = new Random();
    @Autowired
    PostDAO postDAO;
    @Autowired
    UserLockDAO userLockDAO;
    @Autowired
    User2UserTokenDAO user2UserTokenDAO;
    @Autowired
    IAcsClientService iAcsClientService;
    @Autowired
    TokenCache tokenCache;
    @Autowired
    MessageCodeCache messageCodeCache;


    private List<Integer> machineUserList = Lists.newLinkedList();

    @PostConstruct
    public void init() {
        // load all machine userId in memory
        machineUserList = userDAO.getAllMachineUser();
        Logger.COMMON.info("loaded user machine. size={}", machineUserList.size());
    }

    public UserVO getUserInfo(int userId) {
        UserVO user = userDAO.getUser(userId);
        parseUser(user);
        return user;
    }


    public String randomAnonNickName() {
        String anonNickName = "匿名侠" + random.nextInt(1000000);
        return anonNickName;
    }

    public String randomNickName() {
        String anonNickName = "豆豆" + random.nextInt(1000000);
        return anonNickName;
    }


    /**
     * 为帖子对象设置用户头像和用户名
     *
     * @param r
     * @param userId
     */
    public void setUserInfoForPost(PostDetailResp r, int userId) {
        UserVO user = getUserInfo(userId);
        if (r.getAnonymous() == Constants.POST_ANONYMOUS.COMMON) {
            r.setUserName(user.getNickName());
            r.setUserAvatarUrl(user.getAvatarUrl());
        } else {
            r.setUserName(user.getAnonNickName());
            r.setUserAvatarUrl(user.getAnonLouUrl());
        }

    }


    public void setUserInfoForReply(ReplyVO r, int userId) {

        UserVO user = getUserInfo(userId);
        r.setUserName(user.getNickName());
        r.setUserAvatarUrl(user.getAvatarUrl());
    }


    /**
     * 查看封号时间是否到了，尝试解封
     *
     * @param userId
     */
    public void tryToUnlockForTimeout(int userId) {
        UserVO userVO = getUserInfo(userId);
        if (userVO.getIsLock() == Constants.User.Lock.IS_LOCK) {
            UserLockVO userLockVO = userLockDAO.get(userId);
            if (userLockVO.getUnlocktime() < System.currentTimeMillis()) {
                userLockDAO.unlock(userId, Constants.UserLock.UnlockReasonType.TIME_IS_OVER,
                        Constants.UserLock.UnlockOperatorType.SYSTEM,
                        Constants.UserLock.UnlockReason.DEFAULT_TIMEOUT_REASON);
                userDAO.unlockUser(userId);
                Logger.COMMON.info("unlock user successfully. userId={}", userId);
            } else {
                Logger.COMMON.info("user locked. userId={}", userId);
            }
        }
    }

    /**
     * admin操作强行解封
     *
     * @param userId
     * @param unlockReason 需要填写强制解封的原因
     */
    public void forceUnlockByAdmin(int userId, String unlockReason) {
        userLockDAO.unlock(userId,
                Constants.UserLock.UnlockReasonType.ADMIN_FORCE,
                Constants.UserLock.UnlockOperatorType.ADMIN,
                unlockReason);
        userDAO.unlockUser(userId);
        Logger.COMMON.info("unlock user. userId={}", userId);
    }

    /**
     * 封号，一个封号总的入口
     */
    public void lock(int userId, int lockReasonType, int lockOperatorType, String lockReason, long unlockTime) {
        long time = System.currentTimeMillis();
        userLockDAO.lock(userId, lockOperatorType, lockReasonType, lockReason, time, unlockTime);
        userDAO.lockUser(userId);
        Logger.COMMON.info("lock user. userId={},time={},unlockTime={}", userId, DateUtil.format(time), DateUtil.format(unlockTime));
    }

    /**
     * 检测用户是否是封号状态
     *
     * @param userId
     * @return
     */
    public boolean checkLocked(int userId) {
        UserVO user = getUserInfo(userId);
        if (user.getIsLock() == Constants.User.Lock.IS_LOCK) {
            return true;
        } else {
            return false;
        }

    }

    public List<UserVO> list(int pageNum, int size) {
        List<UserVO> list = userDAO.list(pageNum, size);

        for (UserVO user : list) {
            parseUser(user);
        }
        return list;
    }

    public void parseUser(UserVO user) {
        String url = "";
        if (user.getAvatarImgId() == -1) {
            // 没有头像，用默认头像
            url = ossClientService.getFileUrl(Constants.USER_DEFAULT_AVATAR_FILENAME, Constants.IMG_TYPE.AVATAR);
        } else {
            ImgVO img = imgDAO.get(user.getAvatarImgId());
            if (img.getType() == Constants.IMG_TYPE.AVATAR) {
                url = ossClientService.getFileUrl(img.getName(), Constants.IMG_TYPE.AVATAR);
            } else {
                // 微信传过来的头像url，直接使用就可以了
                url = img.getName();
            }
        }
        user.setAvatarUrl(url);
        user.setAnonLouUrl(ossClientService.getFileUrl(Constants.ANON_LOU, Constants.IMG_TYPE.AVATAR));
        user.setAnonXiaUrl(ossClientService.getFileUrl(Constants.ANON_XIA, Constants.IMG_TYPE.AVATAR));
    }

    /**
     * 通过nickname模糊查询
     */
    public List<UserVO> search(String text, int pageNum, int size) {
        List<UserVO> list = userDAO.search(text, pageNum, size);
        return list;
    }

    /**
     * 随机一个机器人用户，主要是为了给抓取数据插入模块使用
     *
     * @return
     */
    public int randomMachineUser() {
        int index = random.nextInt(machineUserList.size());
        return machineUserList.get(index);
    }

    /**
     * 随机一个机器人用户id，但是不能是传入的用户id
     *
     * @param excludeUserId
     * @return
     */
    public int randomMachineUser(int excludeUserId) {
        while (true) {
            int randomUserId = randomMachineUser();
            if (randomUserId != excludeUserId) {
                return randomUserId;

            }
        }
    }

    public int allUserCount() {
        return userDAO.allUserCount();
    }

    public int machineUserCount() {
        return userDAO.machineUserCount();
    }

    public void modifyNameAndAvatar(int userId, String newName, int newImgId) {
        userDAO.modifyNameAndAvatar(userId, newName, newImgId);
    }


    /**
     * 获取对应userId在系统中已经对应好了的token
     * 如果数据库没有的对应的话，返回一个新生成的，如果有userId的话则绑定，没有则直接返回，等登陆的时候完成绑定
     * 如果有对应的话返回，直接返回对应的
     *
     * @param userId
     */
    public String getUserToken(int userId) {
        if (userId == 0) {
            // 如果没有userId，则返回一个生成的userToken
            String userToken = genUserToken();
            LOG.info("userId=0. gen userToken. userToken={}", userToken);
            return userToken;
        } else {
            // 如果有userId的话，查询是否有一个已经存在的
            String userToken = user2UserTokenDAO.getUserToken(userId);
            if (userToken == null) {
                LOG.info("userId={}. no userToken. gen userToken. userToken={}", userId, userToken);
                userToken = genUserToken();
                user2UserTokenDAO.add(userId, userToken);
                return userToken;
            } else {
                LOG.info("userId={}. have userToken. userToken={}", userId, userToken);
                return userToken;
            }
        }

    }

    /**
     * 生成一个userToken，用于未登录状态的用户信息绑定
     *
     * @return
     */
    private String genUserToken() {
        Random random = new Random();
        int ran = random.nextInt(999);
        return "UT" + System.currentTimeMillis() + ran;
    }


    /**
     * 登陆的时候保存客户端的UserToken，如果存在的话更新，如果不存在的话添加相关关系
     *
     * @param userId
     * @param userToken
     */
    public void setUserToken(int userId, String userToken) {
        String tmpUserToken = user2UserTokenDAO.getUserToken(userId);
        if (tmpUserToken == null) {
            user2UserTokenDAO.add(userId, userToken);
        } else {
            user2UserTokenDAO.updateUserToken(userId, userToken);
        }
    }

    /**
     * 通过手机号给用户发送登陆验证码
     * 需要插入到缓存当中
     * 验证码为4位
     *
     * @param mobile
     */
    public void sendMessageCode(String mobile) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10));
        }
        String messageCode = sb.toString();
        messageCodeCache.setMessageCode(mobile, messageCode);
        iAcsClientService.sendMessageCode(mobile, messageCode);
    }

    /**
     * 处理用户基于mobile和验证码的形式进行登陆，如果未注册过会帮助进行注册
     *
     * @param mobile
     * @param messageCode
     */
    public UserMobileMessageCodeLoginResp mobileMessageCodeLogin(String mobile, String messageCode) {
        UserMobileMessageCodeLoginResp resp = new UserMobileMessageCodeLoginResp();
        // 判断mobile是否已经注册过了，如果注册过了，判断这个code是否对应该mobile，否则返回登陆失败
        if (userDAO.checkMobileLogined(mobile)) {
            // 已经注册过，看看是否可以登陆成功

            String cachedMessageCode = messageCodeCache.getMessageCode(mobile);
            if(cachedMessageCode==null)
                throw new CommonException(ErrorCode.User.MESSAGE_CODE_WRONG);
            if (cachedMessageCode.equals(messageCode)) {
                messageCodeCache.deleteMessageCode(mobile);
                // 可以进行登陆
                int userId = userDAO.getUserIdByMobile(mobile);
                String token = resetTokenCache(userId);
                resp.setT(token);
                resp.setUserId(userId);
                return resp;
            } else {
                // 验证不通过
                throw new CommonException(ErrorCode.User.MESSAGE_CODE_WRONG);
            }
        } else {
            String cachedMessageCode = messageCodeCache.getMessageCode(mobile);
            if(cachedMessageCode==null)
                throw new CommonException(ErrorCode.User.MESSAGE_CODE_WRONG);
            if (cachedMessageCode.equals(messageCode)) {
                // 没有注册过，需要注册   默认的图片为25
                int userId = userDAO.register(randomNickName(), randomAnonNickName(), 25, Constants.USER_SOURCE_TYPE.MOBILE_MESSAGE_CODE, mobile);
                // 全都设置成默认值
                userDAO.registerUserInfoFromWechat(userId, "", "", "", -1, "");
                String token = resetTokenCache(userId);
                resp.setT(token);
                resp.setUserId(userId);
                messageCodeCache.deleteMessageCode(mobile);
            }else{
                throw new CommonException(ErrorCode.User.MESSAGE_CODE_WRONG);
            }
        }
        return resp;
    }

    /**
     * 用于注册和登陆时候的重置t以及t和userId的所有对应关系
     *
     * @param userId
     * @return 返回对应的新的token
     */
    public String resetTokenCache(int userId) {
        // 删除老的token和userId2Token
        String oldT = tokenCache.getUserId2Token(userId);
        tokenCache.deleteToken2UserId(oldT);
        tokenCache.deleteUserId2Token(userId);
        LOG.debug("clean old token. token={}, userId={}", oldT, userId);

        // 生成一个新的token
        String token = tokenCache.genToken();

        tokenCache.setUserId2Token(userId, token);
        tokenCache.setToken2UserId(token, userId);
        LOG.debug("add new token. token={},userId={}", new Object[]{token, userId});
        return token;
    }
}
