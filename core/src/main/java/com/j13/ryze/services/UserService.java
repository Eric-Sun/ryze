package com.j13.ryze.services;

import com.j13.ryze.api.req.PostDetailResp;
import com.j13.ryze.api.resp.Level2ReplyDetailResp;
import com.j13.ryze.api.resp.ReplyDetailResp;
import com.j13.ryze.core.Constants;
import com.j13.ryze.daos.ImgDAO;
import com.j13.ryze.daos.PostDAO;
import com.j13.ryze.daos.UserDAO;
import com.j13.ryze.daos.UserLockDAO;
import com.j13.ryze.vos.ImgVO;
import com.j13.ryze.vos.PostVO;
import com.j13.ryze.vos.UserLockVO;
import com.j13.ryze.vos.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {

    private static Logger LOG = LoggerFactory.getLogger(UserService.class);
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


    public UserVO getUserInfo(int userId) {
        UserVO user = userDAO.getUser(userId);
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

        return user;
    }


    public String randomAnonNickName() {
        String anonNickName = "匿名侠" + random.nextInt(1000000);
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


    public void setUserInfoForReply(int postAnonymous, ReplyDetailResp r, int userId) {

        UserVO user = getUserInfo(userId);
        if (postAnonymous == Constants.REPLY_ANONYMOUS.COMMON) {
            if (r.getAnonymous() == Constants.REPLY_ANONYMOUS.COMMON) {
                r.setUserName(user.getNickName());
                r.setUserAvatarUrl(user.getAvatarUrl());
            } else {
                r.setUserName(user.getAnonNickName());
                r.setUserAvatarUrl(user.getAnonLouUrl());
            }
        } else {
            r.setUserName(user.getAnonNickName());
            r.setUserAvatarUrl(user.getAnonLouUrl());
        }
    }

    public void setUserInfoForReply(int postAnonymous, Level2ReplyDetailResp r, int userId) {
        UserVO user = getUserInfo(userId);
        if (postAnonymous == Constants.REPLY_ANONYMOUS.COMMON) {
            if (r.getAnonymous() == Constants.REPLY_ANONYMOUS.COMMON) {
                r.setUserName(user.getNickName());
                r.setUserAvatarUrl(user.getAvatarUrl());
            } else {
                r.setUserName(user.getAnonNickName());
                r.setUserAvatarUrl(user.getAnonLouUrl());
            }
        } else {
            r.setUserName(user.getAnonNickName());
            r.setUserAvatarUrl(user.getAnonLouUrl());
        }
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
                LOG.info("unlock user. userId={}", userId);
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
        LOG.info("unlock user. userId={}", userId);
    }

    /**
     * 封号，一个封号总的入口
     */
    public void lock(int userId, int lockReasonType, int lockOperatorType, String lockReason, long unlockTime) {
        long time = System.currentTimeMillis();
        userLockDAO.lock(userId, lockOperatorType, lockReasonType, lockReason, time, unlockTime);
        LOG.info("lock user. userId={},time={},unlocktime={}", userId, time, unlockTime);
    }
}
