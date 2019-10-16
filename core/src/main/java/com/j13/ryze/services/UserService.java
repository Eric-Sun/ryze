package com.j13.ryze.services;

import com.google.common.collect.Lists;
import com.j13.ryze.api.req.PostDetailResp;
import com.j13.ryze.api.resp.Level2ReplyDetailResp;
import com.j13.ryze.api.resp.ReplyDetailResp;
import com.j13.ryze.core.Constants;
import com.j13.ryze.core.Logger;
import com.j13.ryze.daos.ImgDAO;
import com.j13.ryze.daos.PostDAO;
import com.j13.ryze.daos.UserDAO;
import com.j13.ryze.daos.UserLockDAO;
import com.j13.ryze.utils.DateUtil;
import com.j13.ryze.vos.*;
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

    private List<Integer> machineUserList = Lists.newLinkedList();

    @PostConstruct
    public void init() {
        // load all machine userId in memory
        machineUserList = userDAO.getAllMachineUser();
        Logger.COMMON.info("loaded user machine. size={}", machineUserList.size());
    }

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
        }
        return list;
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
        userDAO.modifyNameAndAvatar(userId,newName,newImgId);
    }
}
