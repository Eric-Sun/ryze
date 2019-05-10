package com.j13.ryze.services;

import com.aliyun.oss.OSSClient;
import com.j13.poppy.config.PropertiesConfiguration;
import com.j13.poppy.exceptions.ServerException;
import com.j13.ryze.core.Constants;
import com.j13.ryze.daos.ImgDAO;
import com.j13.ryze.vos.ImgVO;
import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.Random;

@Service
public class ImgService {

    private static Logger LOG = LoggerFactory.getLogger(ImgService.class);
    @Autowired
    ImgDAO imgDAO;

    @Autowired
    OSSClientService ossClientService;

    Random ran = new Random();

    public ImgVO saveFile(FileItem item, int type) {
        String fileName = ossClientService.saveFile(item, type);
        ImgVO img = new ImgVO();
        int imgId = insertImg(fileName, type);
        img.setId(imgId);
        img.setName(fileName);
        img.setType(type);
        String url = ossClientService.getFileUrl(img.getName(), img.getType());
        img.setUrl(url);
        return img;
    }

    /**
     * 用于存储来源于wechat的头像
     *
     * @param url
     * @return
     */
    public int saveWechatAvatar(String url) {
        int imgId = insertImg(url, Constants.IMG_TYPE.AVATAR_URL_FROM_WECHAT);
        return imgId;
    }

    public void deleteOldWechatAvatar(int imgId) {
        imgDAO.delete(imgId);
    }


    public String getFileUrl(int imgId) {
        ImgVO imgVO = imgDAO.get(imgId);
        String url = ossClientService.getFileUrl(imgVO.getName(), imgVO.getType());
        return url;
    }

    private int insertImg(String fileName, int type) {
        return imgDAO.insert(fileName, type);
    }


    public String getWechatUrlFromImgId(int imgId) {
        String name = imgDAO.getName(imgId);
        return name;
    }
}
