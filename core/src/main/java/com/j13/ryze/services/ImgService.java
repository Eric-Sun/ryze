package com.j13.ryze.services;

import com.aliyun.oss.OSSClient;
import com.j13.poppy.config.PropertiesConfiguration;
import com.j13.poppy.exceptions.ServerException;
import com.j13.ryze.core.Constants;
import com.j13.ryze.daos.ImgDAO;
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

    public int saveFile(FileItem item, int type) {
        String fileName = ossClientService.saveFile(item, type);
        int imgId = insertImg(fileName, type);
        return imgId;
    }


    private int insertImg(String fileName, int type) {
        return imgDAO.insert(fileName, type);
    }


}
