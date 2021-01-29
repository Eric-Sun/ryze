package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.AdminBannerAddReq;
import com.j13.ryze.api.req.AdminBannerListReq;
import com.j13.ryze.api.resp.*;
import com.j13.ryze.services.BannerService;
import com.j13.ryze.services.ImgService;
import com.j13.ryze.vos.BannerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminBannerFacade {

    @Autowired
    BannerService bannerService;
    @Autowired
    ImgService imgService;


    @Action(name="adminBanner.add")
    public CommonResultResp add(CommandContext ctxt, AdminBannerAddReq req) {
        bannerService.addBanner(req.getName(), req.getUrlImgId());
        return CommonResultResp.success();
    }
    @Action(name="adminBanner.delete")
    public CommonResultResp delete(CommandContext ctxt, AdminBannerDeleteReq req) {
        bannerService.deleteBanner(req.getId());
        return CommonResultResp.success();
    }
    @Action(name="adminBanner.update")
    public CommonResultResp update(CommandContext ctxt, AdminBannerUpdateReq req) {
        bannerService.updateBanner(req.getId(), req.getName(), req.getUrlImgId());
        return CommonResultResp.success();
    }
    @Action(name="adminBanner.list")
    public AdminBannerListResp list(CommandContext ctxt, AdminBannerListReq req) {
        List<BannerVO> bannerList = bannerService.listBanner(req.getSize(), req.getPageNum());
        AdminBannerListResp resp = new AdminBannerListResp();
        for (BannerVO vo : bannerList) {
            AdminBannerDetailResp detailResp = new AdminBannerDetailResp();
            BeanUtils.copyProperties(detailResp, vo);

            ImgDetailResp imgResp = new ImgDetailResp();
            imgResp.setImgId(vo.getUrlImgId());
            imgResp.setUrl(vo.getImg().getUrl());
            detailResp.setImgDetailResp(imgResp);
            resp.getBannerList().add(detailResp);
        }
        return resp;
    }


}
