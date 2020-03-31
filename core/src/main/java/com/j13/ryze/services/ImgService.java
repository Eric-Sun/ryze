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
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
     * 通过网络图片url存储在库中
     *
     * @param imgUrl
     * @param type
     * @return
     */
    public ImgVO saveFile(String imgUrl, int type) {
        HttpURLConnection conn = null;
        URL url = null;
        InputStream inputStream = null;

        try {
            url = new URL(imgUrl);


            conn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            conn.setConnectTimeout(3000);

            // 正常响应时获取输入流, 在这里也就是图片对应的字节流
            if (conn.getResponseCode() == 200) {
                inputStream = conn.getInputStream();

                String fileName = ossClientService.saveFile(inputStream, type);
                ImgVO img = new ImgVO();
                int imgId = insertImg(fileName, type);
                img.setId(imgId);
                img.setName(fileName);
                img.setType(type);
                return img;
            } else {
                return null;
            }


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.disconnect();
        }

    }

    /**
     * 通过网络图片url存储在库中
     *
     * @param imgUrl
     * @param type
     * @return
     */
    public ImgVO saveFileWithFileName(String fileName, String imgUrl, int type) {
        HttpURLConnection conn = null;
        URL url = null;
        InputStream inputStream = null;

        try {
            url = new URL(imgUrl);


            conn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            conn.setConnectTimeout(3000);

            // 正常响应时获取输入流, 在这里也就是图片对应的字节流
            if (conn.getResponseCode() == 200) {
                inputStream = conn.getInputStream();

                fileName = ossClientService.saveFile(fileName, inputStream, type);
                ImgVO img = new ImgVO();
                int imgId = insertImg(fileName, type);
                img.setId(imgId);
                img.setName(fileName);
                img.setType(type);
                return img;
            } else {
                return null;
            }


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.disconnect();
        }

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


    /**
     * 用于存储来源于头条的头像
     *
     * @param url
     * @return
     */
    public int saveToutiaoAvatar(String url) {
        int imgId = insertImg(url, Constants.IMG_TYPE.AVATAR_URL_FROM_TOUTIAO);
        return imgId;
    }

    /**
     * 用于存储来源于头条的头像
     *
     * @param url
     * @return
     */
    public int saveBaiduAvatar(String url) {
        int imgId = insertImg(url, Constants.IMG_TYPE.AVATAR_URL_FROM_BAIDU);
        return imgId;
    }


    public void deleteOldWechatAvatar(int imgId) {
        imgDAO.delete(imgId);
    }


    /**
     * 通过imgId获得播放url
     *
     * @param imgId
     * @return
     */
    public String getFileUrl(int imgId) {
        ImgVO imgVO = imgDAO.get(imgId);
        String url = ossClientService.getFileUrl(imgVO.getName(), imgVO.getType());
        return url;
    }

    private int insertImg(String fileName, int type) {
        return imgDAO.insert(fileName, type);
    }


    public String getUrlFromImgId(int imgId) {
        String name = imgDAO.getName(imgId);
        return name;
    }
}
