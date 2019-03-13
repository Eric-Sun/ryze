package com.j13.ryze.services;

import com.j13.ryze.core.Constants;
import com.j13.ryze.daos.UserDAO;
import com.j13.ryze.vos.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    UserDAO userDAO;
    @Autowired
    OSSClientService ossClientService;

    public UserVO getUserInfo(int userId) {
        UserVO user = userDAO.getUser(userId);
        if (user.getAvatarUrl().equals("")) {
            String url = ossClientService.getFileUrl(Constants.USER_DEFAULT_AVATAR_FILENAME, Constants.IMG_TYPE.AVATAR);
            user.setAvatarUrl(url);
        }
        return user;
    }


}
