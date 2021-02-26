package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.ryze.api.req.BannerGetInfoReq;
import com.j13.ryze.api.resp.BannerGetInfoResp;
import com.j13.ryze.api.resp.BannerGetResp;
import com.j13.ryze.services.BannerService;
import com.j13.ryze.vos.BannerPlanVO;
import com.j13.ryze.vos.BannerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BannerFacade {

    @Autowired
    BannerService bannerService;

    @Action(name = "banner.getInfo")
    public BannerGetInfoResp getInfo(CommandContext ctxt, BannerGetInfoReq req) {

        BannerGetInfoResp resp = new BannerGetInfoResp();
        BannerPlanVO bpVO = bannerService.getBannerPlan(req.getBannerPlanId());
        for (BannerVO bannerVO : bpVO.getBannerVOList()) {
            BannerGetResp getResp = new BannerGetResp();
            getResp.setId(bannerVO.getId());
            getResp.setImgUrl(bannerVO.getImg().getUrl());
            resp.getData().add(getResp);
        }
        return resp;
    }

}
