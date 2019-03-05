package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.ryze.api.req.AdminBarQueryReq;
import com.j13.ryze.api.req.AdminUserTxtLoadReq;
import com.j13.ryze.api.resp.AdminBarQueryResp;
import com.j13.ryze.core.Constants;
import com.j13.ryze.daos.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Random;

@Component
public class AdminUserFacade {

    private static Logger LOG = LoggerFactory.getLogger("admin.user.load");

    @Autowired
    UserDAO userDAO;
    private Random random = new Random();

    @Action(name = "admin.user.txtLoad", desc = "")
    public CommonResultResp txtLoad(CommandContext ctxt, AdminUserTxtLoadReq req) {
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(new File(req.getPath())));
            File outputFile = new File(req.getPath()+".done");
            outputFile.createNewFile();
            bw = new BufferedWriter(new FileWriter(outputFile));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] strs = line.split(",");
                String nickName = strs[0];
                int sex = strs[1].equals("男") ? Constants.USER_SEX.MALE : Constants.USER_SEX.FEMALE;
                String anonNickName = "匿名侠" + random.nextInt(1000000);
                int userId = userDAO.add(nickName, anonNickName, sex, Constants.USER_IS_MACHINE.MACHINE);
                String newLine = strs[0] + "," + strs[1] + "," + userId + "\n";
                bw.write(newLine);
            }
        } catch (Exception e) {
            e.printStackTrace();;
            return CommonResultResp.failure();
        } finally {
            try {
                br.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();;
                CommonResultResp.failure();
            }
        }

        return CommonResultResp.success();
    }
}
