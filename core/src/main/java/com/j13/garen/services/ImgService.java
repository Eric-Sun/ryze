package com.j13.garen.services;

import com.j13.garen.daos.ImgDAO;
import com.j13.garen.vos.ImgVO;
import com.j13.poppy.config.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImgService {

    @Autowired
    ImgDAO imgDAO;


    public ImgVO loadImg(int imgId){
        ImgVO imgVO = imgDAO.get(imgId);
        String imgServer = PropertiesConfiguration.getInstance().getStringValue("img.server");
        imgVO.setSrc(imgServer+ imgVO.getName());
        return imgVO;
    }



}
