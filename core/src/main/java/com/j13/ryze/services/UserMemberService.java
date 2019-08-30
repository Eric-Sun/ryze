package com.j13.ryze.services;

import com.j13.ryze.daos.UserMemberDAO;
import com.j13.ryze.vos.UserMemberVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserMemberService {
    private static Logger LOG = LoggerFactory.getLogger(UserMemberService.class);


    @Autowired
    UserMemberDAO userMemberDAO;

    /**
     * 尝试回去用户的会员信息，如果没有会员或者会员过期返回null
     *
     * @param userId
     * @return
     */
    public UserMemberVO getUserMember(int userId) {
        UserMemberVO vo = userMemberDAO.getUserMember(userId);
        if (vo.getExpiretime() > System.currentTimeMillis()) {
            return vo;
        } else {
            return null;
        }
    }

    /**
     * 充值会员，判断是否有会员记录，如果有记录的话修改过期时间和级别，如果没有的话增加这条记录
     * 过期时间
     * @param userId
     * @param level
     */
    public void chargeUserMember(int userId, int level,long expiretime) {
        // check exist
        int count = userMemberDAO.checkExist(userId);
        if (count == 0) {
            userMemberDAO.add(userId, level, expiretime);
        } else {
            userMemberDAO.updateUserMember(userId, level, expiretime);
        }
    }





}
