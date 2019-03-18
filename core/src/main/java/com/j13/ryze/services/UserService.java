package com.j13.ryze.services;

import com.j13.ryze.core.Constants;
import com.j13.ryze.daos.ImgDAO;
import com.j13.ryze.daos.UserDAO;
import com.j13.ryze.vos.ImgVO;
import com.j13.ryze.vos.UserVO;
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

        return user;
    }


    public String randomAnonNickName() {
        String anonNickName = "匿名侠" + random.nextInt(1000000);
        return anonNickName;
    }


}
