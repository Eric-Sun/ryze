package com.j13.ryze.services;

import com.aliyun.oss.OSSClient;
import com.j13.poppy.config.PropertiesConfiguration;
import com.j13.poppy.exceptions.CommonException;
import com.j13.poppy.exceptions.ServerException;
import com.j13.ryze.core.Constants;
import com.j13.ryze.core.ErrorCode;
import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Random;

@Service
public class OSSClientService {

    private static Logger LOG = LoggerFactory.getLogger(OSSClientService.class);
    private OSSClient ossClient = null;
    private Random random = new Random();
    private String endpoint = null;
    private String accessKeyId = null;
    private String accessKeySecret = null;
    private String bucketName = null;

    @PostConstruct
    public void init() {
        endpoint = PropertiesConfiguration.getInstance().getStringValue("ossclient.endpoint");
        accessKeyId = PropertiesConfiguration.getInstance().getStringValue("ossclient.accessKeyId");
        accessKeySecret = PropertiesConfiguration.getInstance().getStringValue("ossclient.accessKeySecret");
        bucketName = PropertiesConfiguration.getInstance().getStringValue("ossclient.bucketName");
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        LOG.info("ossclient init successfully.");
    }


    /**
     * 保存文件，反馈文件名
     * @param item
     * @param type
     * @return
     */
    public String saveFile(FileItem item, int type) {
        try {
            InputStream in = item.getInputStream();
            String originalFilename = item.getName();
            String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            String fileName = random.nextInt(10000) + System.currentTimeMillis() + substring;
            String fullFileName = findDir(type) + "/" + fileName;
            ossClient.putObject(bucketName, fullFileName, in);
            ossClient.shutdown();
            in.close();
            item.delete();
            return fileName;
        } catch (IOException e) {
            throw new CommonException(ErrorCode.Img.UPLOAD_IMG_ERROR, e);
        }
    }



    /**
     * 获取图片类型对韵存储在oss上的目录名称
     *
     * @param type
     * @return
     */
    private String findDir(int type) {
        if (type == Constants.IMG_TYPE.AVATAR) {
            return "avatar";
        } else {
            throw new CommonException(ErrorCode.Img.TYPE_IS_WRONG, "type is " + type);
        }
    }

}
