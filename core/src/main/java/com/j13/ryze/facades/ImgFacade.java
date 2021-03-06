package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.ryze.api.req.ImgUploadReq;
import com.j13.ryze.api.resp.ImgUploadResp;
import com.j13.ryze.core.Constants;
import com.j13.ryze.services.ImgService;
import com.j13.ryze.vos.ImgVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImgFacade {

    private static Logger LOG = LoggerFactory.getLogger(ImgFacade.class);

    @Autowired
    ImgService imgService;

    @Action(name = "img.upload", desc = "Constants.IMG_TYPE")
    public ImgUploadResp upload(CommandContext ctxt, ImgUploadReq req) {
        ImgUploadResp resp = new ImgUploadResp();
        ImgVO imgVO = null;
//        if (req.getType() == Constants.IMG_TYPE.AVATAR) {
        imgVO = imgService.saveFile(req.getFile(), req.getType());
        LOG.info("save img. imgId={},type={}", imgVO.getId(), req.getType());
//        }
        resp.setImgId(imgVO.getId());
        resp.setUrl(imgVO.getUrl());
        return resp;
    }


}
