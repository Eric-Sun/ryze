package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.exceptions.CommonException;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.*;
import com.j13.ryze.api.resp.AdminBarQueryResp;
import com.j13.ryze.api.resp.AdminUserDetailResp;
import com.j13.ryze.api.resp.AdminUserListResp;
import com.j13.ryze.api.resp.AdminUserSearchResp;
import com.j13.ryze.core.Constants;
import com.j13.ryze.core.ErrorCode;
import com.j13.ryze.daos.UserDAO;
import com.j13.ryze.services.UserService;
import com.j13.ryze.vos.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Random;

@Component
public class AdminUserFacade {

    private static Logger LOG = LoggerFactory.getLogger("admin.user.load");

    @Autowired
    UserDAO userDAO;
    private Random random = new Random();
    @Autowired
    UserService userService;


    @Action(name = "admin.user.txtLoad", desc = "通过txt文件添加用户")
    public CommonResultResp txtLoad(CommandContext ctxt, AdminUserTxtLoadReq req) {
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(new File(req.getPath())));
            File outputFile = new File(req.getPath() + ".done");
            outputFile.createNewFile();
            bw = new BufferedWriter(new FileWriter(outputFile));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] strs = line.split(",");
                String nickName = strs[0];
                int sex = strs[1].equals("男") ? Constants.USER_SEX.MALE : Constants.USER_SEX.FEMALE;
                String anonNickName = "匿名侠" + random.nextInt(1000000);
                int userId = userDAO.register(nickName, anonNickName, -1, Constants.USER_SOURCE_TYPE.MACHINE);
                // 插入到user_info表中
                userDAO.updateInfoFromWechat(userId, "", "", "", sex, "");
                String newLine = strs[0] + "," + strs[1] + "," + userId + "\n";
                bw.write(newLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ;
            return CommonResultResp.failure();
        } finally {
            try {
                br.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
                ;
                CommonResultResp.failure();
            }
        }
        return CommonResultResp.success();
    }


    @Action(name = "admin.user.lock", desc = "提供给admin系统封号")
    public CommonResultResp lock(CommandContext ctxt, AdminUserLockReq req) {
        if (!userService.checkLocked(req.getLockUserId())) {
            userService.lock(req.getLockUserId(), req.getLockReasonType(), Constants.UserLock.LockOperatorType.ADMIN, req.getLockReason(), req.getUnlockTime());
            LOG.info("user lock successfully. userId={}", req.getLockUserId());
        } else {
            LOG.info("user has been locked. userId={}", req.getLockUserId());
            throw new CommonException(ErrorCode.User.USER_HAS_BEEN_LOCKED);
        }
        return CommonResultResp.success();
    }


    @Action(name = "admin.user.unlock", desc = "提供给admin系统解封账号")
    public CommonResultResp unlock(CommandContext ctxt, AdminUserUnlockReq req) {
        if (userService.checkLocked(req.getUnlockUserId())) {
            userService.forceUnlockByAdmin(req.getUnlockUserId(), req.getUnlockReason());
            LOG.info("user unlock successfully. userId={}", req.getUnlockUserId());
        } else {
            LOG.info("user has been unlocked. userId={}", req.getUnlockUserId());
            throw new CommonException(ErrorCode.User.USER_HAS_BEEN_UNLOCKED);
        }
        return CommonResultResp.success();
    }


    @Action(name = "admin.user.list", desc = "用户列表")
    public AdminUserListResp list(CommandContext ctxt, AdminUserListReq req) {
        AdminUserListResp resp = new AdminUserListResp();
        List<UserVO> userList = userService.list(req.getPageNum(), req.getSize());
        for (UserVO user : userList) {
            AdminUserDetailResp detail = new AdminUserDetailResp();
            BeanUtils.copyProperties(detail, user);
            resp.getList().add(detail);
        }
        return resp;
    }


    @Action(name = "admin.user.search", desc = "模糊查询用户信息")
    public AdminUserSearchResp list(CommandContext ctxt, AdminUserSearchReq req) {
        AdminUserSearchResp resp = new AdminUserSearchResp();
        List<UserVO> userList = userService.search(req.getText(),req.getPageNum(),req.getSize());
        for (UserVO user : userList) {
            AdminUserDetailResp detail = new AdminUserDetailResp();
            BeanUtils.copyProperties(detail, user);
            resp.getList().add(detail);
        }
        return resp;
    }

}
