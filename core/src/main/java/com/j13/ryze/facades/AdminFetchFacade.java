package com.j13.ryze.facades;

import com.google.common.base.Splitter;
import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.ryze.api.req.AdminFetchUserFromQSBKReq;
import com.j13.ryze.api.req.AdminFetchUserFromTianyaReq;
import com.j13.ryze.api.resp.AdminFetchUserFromQSBKResp;
import com.j13.ryze.api.resp.AdminFetchUserFromTianyaResp;
import com.j13.ryze.core.Constants;
import com.j13.ryze.daos.UserDAO;
import com.j13.ryze.services.ImgService;
import com.j13.ryze.utils.InternetUtil;
import com.j13.ryze.vos.ImgVO;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Iterator;
import java.util.Random;

@Component
public class AdminFetchFacade {
    private static Logger LOG = LoggerFactory.getLogger(AdminFetchFacade.class);

    @Autowired
    UserDAO userDAO;
    @Autowired
    ImgService imgService;

    private Random random = new Random();

    // 去掉ty
    @Action(name = "admin.fetchUser.fromTianya")
    public AdminFetchUserFromTianyaResp fetchUserFromTianya(CommandContext ctxt, AdminFetchUserFromTianyaReq req) {
        AdminFetchUserFromTianyaResp resp = new AdminFetchUserFromTianyaResp();
        int count = req.getCount();
        int successCount = 0;
        int from = req.getFrom();
        //140364877
        int currentId = from;

        while (successCount != count) {
            String rawString = InternetUtil.get("http://www.tianya.cn/" + currentId);
            int userNickNameIndexStart = rawString.indexOf("<title>") + "<title>".length();
            int userNickNameIndexEnd = rawString.indexOf("</title>", userNickNameIndexStart);
            String userNickName = rawString.substring(userNickNameIndexStart, userNickNameIndexEnd);
            userNickName = userNickName.replaceAll("_天涯社区", "");
            userNickName = userNickName.replaceAll("ty_", "");
            if (userNickName.equals("出错了")) {
                LOG.info("error. currentId={}", currentId);
                continue;
            }

            String userImgUrl = "http://tx.tianyaui.com/logo/" + currentId;

            boolean exist = userDAO.checkNickNameExisted(userNickName);
            if (exist) {
                LOG.info("userName={},existed. so continue. currentId={}", userNickName, currentId);
                currentId++;
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            ImgVO imgVO = imgService.saveFile(userImgUrl, Constants.IMG_TYPE.AVATAR);

            String anonNickName = "匿名侠" + random.nextInt(1000000);
            // 插入到user_info表中
            int userId = userDAO.register(userNickName, anonNickName, imgVO.getId(), Constants.USER_SOURCE_TYPE.MACHINE);

            userDAO.registerUserInfoFromWechat(userId, "Chaoyang", "China", "Beijing", Constants.User.Gender.NO, "zh_CN");
            LOG.info("get userName={}  currentId={}", userNickName, currentId);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentId++;
            successCount++;
        }
        resp.setSuccessCount(successCount);
        return resp;
    }
}
