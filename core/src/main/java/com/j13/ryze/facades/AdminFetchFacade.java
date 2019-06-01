package com.j13.ryze.facades;

import com.google.common.base.Splitter;
import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.ryze.api.req.AdminFetchUserFromQSBKReq;
import com.j13.ryze.api.resp.AdminFetchUserFromQSBKResp;
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

    @Action(name = "admin.fetchUser.fromQSBK")
    public AdminFetchUserFromQSBKResp fetchUserFromQSBK(CommandContext ctxt, AdminFetchUserFromQSBKReq req) {
        AdminFetchUserFromQSBKResp resp = new AdminFetchUserFromQSBKResp();
        int count = 0;
        if (!req.getToken().equals("84658544"))
            return resp;

        String fetchUrl = "http://www.qiushibaike.com/text/page/" + req.getPageNum() + "/?s=3232322222";
        LOG.info("fetch url = " + fetchUrl);

        String rawResponse = InternetUtil.get(fetchUrl);

        Iterator<String> iter = Splitter.on("target=\"_blank\" rel=\"nofollow\" style=\"height: 35px\" onclick=\"_hmt.push(['_trackEvent','web-list-author-img','chick'])\">\n" +
                "\n" +
                "<img src=\"").split(rawResponse).iterator();
        iter.next();
        while (iter.hasNext()) {
            String splitterStr = iter.next();
            int imgTailIndex = splitterStr.indexOf("\" alt=\"");
            if (imgTailIndex == -1)
                continue;
            String userImgUrl = "https:" + splitterStr.substring(0, imgTailIndex).replaceAll("\\?imageView2/1/w/90/h/90", "");


            int userNameIndex = splitterStr.indexOf("onclick=\"_hmt.push(['_trackEvent','web-list-author-text','chick'])\">\n" +
                    "<h2>");
            int userNameFromIndex = userNameIndex + new Integer("onclick=\"_hmt.push(['_trackEvent','web-list-author-text','chick'])\">\n<h2>".length());
            splitterStr = splitterStr.substring(userNameFromIndex);
            int userNameLastIndex = splitterStr.indexOf("</h2>");
            String userName = splitterStr.substring(0, userNameLastIndex).replaceAll("\\n", "");

            int gender = 1;
            int sexIndex = splitterStr.indexOf("<div class=\"articleGender ") + "<div class=\"articleGender ".length();
            String sexSign = splitterStr.substring(sexIndex, sexIndex + 3);
            if (sexSign.equals("wom")) {
                gender = 2;
            }

            boolean exist = userDAO.checkNickNameExisted(userName);
            if (exist) {
                LOG.info("userName={},existed. so continue.", userName);
                continue;
            }

            ImgVO imgVO = imgService.saveFile(userImgUrl, Constants.IMG_TYPE.AVATAR);

            String anonNickName = "匿名侠" + random.nextInt(1000000);
            // 插入到user_info表中
            int userId = userDAO.register(userName, anonNickName, imgVO.getId(), Constants.USER_SOURCE_TYPE.MACHINE);

            userDAO.registerUserInfoFromWechat(userId, "Chaoyang", "China", "Beijing", gender, "zh_CN");

            LOG.info("userId={},userName={},imgUrl={},gender={}", userId, userName, userImgUrl, gender);
            count++;

        }

        resp.setSuccessCount(count);
        return resp;
    }


}
