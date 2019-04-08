package com.j13.ryze.services;

import com.j13.ryze.api.req.PostDetailResp;
import com.j13.ryze.api.resp.Level2ReplyDetailResp;
import com.j13.ryze.api.resp.ReplyDetailResp;
import com.j13.ryze.core.Constants;
import com.j13.ryze.daos.ImgDAO;
import com.j13.ryze.daos.PostDAO;
import com.j13.ryze.daos.UserDAO;
import com.j13.ryze.vos.ImgVO;
import com.j13.ryze.vos.PostVO;
import com.j13.ryze.vos.UserVO;
import com.sun.tools.internal.jxc.ap.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

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
        if (postAnonymous==Constants.REPLY_ANONYMOUS.COMMON){
            if (r.getAnonymous() == Constants.REPLY_ANONYMOUS.COMMON) {
                r.setUserName(user.getNickName());
                r.setUserAvatarUrl(user.getAvatarUrl());
            } else {
                r.setUserName(user.getAnonNickName());
                r.setUserAvatarUrl(user.getAnonLouUrl());
            }
        }else{
            r.setUserName(user.getAnonNickName());
            r.setUserAvatarUrl(user.getAnonLouUrl());
        }
    }

    public void setUserInfoForReply(int postAnonymous,Level2ReplyDetailResp r, int userId) {
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
}
