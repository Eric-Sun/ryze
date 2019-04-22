package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.ryze.api.req.ImgUploadReq;
import com.j13.ryze.api.resp.ImgUploadResp;
import com.j13.ryze.core.Constants;
import com.j13.ryze.services.ImgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImgFacade {

    private static Logger LOG = LoggerFactory.getLogger(ImgFacade.class);

    @Autowired
    ImgService imgService;

    @Action(name = "img.upload", desc = "")
    public ImgUploadResp upload(CommandContext ctxt, ImgUploadReq req) {
        ImgUploadResp resp = new ImgUploadResp();
        int imgId = 0;
        if (req.getType() == Constants.IMG_TYPE.AVATAR) {
            imgId = imgService.saveFile(req.getFile(), req.getType());
            LOG.info("save avatar img. imgId={}", imgId);
        }
        resp.setImgId(imgId);
        return resp;
    }




}
