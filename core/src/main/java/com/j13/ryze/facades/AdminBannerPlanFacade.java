package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.*;
import com.j13.ryze.api.resp.AdminBannerDetailResp;
import com.j13.ryze.api.resp.AdminBannerPlanDetailResp;
import com.j13.ryze.api.resp.AdminBannerPlanListResp;
import com.j13.ryze.api.resp.ImgDetailResp;
import com.j13.ryze.services.BannerService;
import com.j13.ryze.vos.BannerPlanVO;
import com.j13.ryze.vos.BannerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminBannerPlanFacade {

    @Autowired
    BannerService bannerService;


    @Action(name = "adminBannerPlan.add")
    public CommonResultResp add(CommandContext ctxt, AdminBannerPlanAddReq req) {
        bannerService.addBannerPlan(req.getName(), req.getType());
        return CommonResultResp.success();
    }

    @Action(name = "adminBannerPlan.delete")
    public CommonResultResp delete(CommandContext ctxt, AdminBannerPlanDeleteReq req) {
        bannerService.deleteBannerPlan(req.getId());
        return CommonResultResp.success();
    }

    @Action(name = "adminBannerPlan.update")
    public CommonResultResp update(CommandContext ctxt, AdminBannerPlanUpdateReq req) {
        bannerService.updateBannerPlan(req.getId(), req.getName());
        return CommonResultResp.success();
    }

    @Action(name = "adminBannerPlan.addBannner")
    public CommonResultResp addBannner(CommandContext ctxt, AdminBannerPlanAddBannerReq req) {
        bannerService.addBannerToBannerPlan(req.getBannerPlanId(), req.getBannerId());
        return CommonResultResp.success();
    }

    @Action(name = "adminBannerPlan.removeBanner")
    public CommonResultResp removeBanner(CommandContext ctxt, AdminBannerPlanRemoveBannerReq req) {
        bannerService.deleteBannerFromBannerPlan(req.getBannerPlanId(), req.getBannerId());
        return CommonResultResp.success();
    }

    @Action(name = "adminBannerPlan.list")
    public AdminBannerPlanListResp list(CommandContext ctxt, AdminBannerPlanListReq req) {
        AdminBannerPlanListResp resp = new AdminBannerPlanListResp();
        List<BannerPlanVO> bannerPlanVOList = bannerService.bannerPlanList(req.getSize(), req.getPageNum());
        for (BannerPlanVO bannerPlanVO : bannerPlanVOList) {
            AdminBannerPlanDetailResp bannerPlanDetailResp = new AdminBannerPlanDetailResp();
            BeanUtils.copyProperties(bannerPlanDetailResp,bannerPlanVO);
            for(BannerVO bannerVO : bannerPlanVO.getBannerVOList()){
                AdminBannerDetailResp adminBannerDetailResp = new AdminBannerDetailResp();
                BeanUtils.copyProperties(adminBannerDetailResp,bannerVO);
                ImgDetailResp imgResp = new ImgDetailResp();
                imgResp.setImgId(bannerVO.getUrlImgId());
                imgResp.setUrl(bannerVO.getImg().getUrl());
                adminBannerDetailResp.setImgDetailResp(imgResp);
                bannerPlanDetailResp.getBannerRespList().add(adminBannerDetailResp);
                bannerPlanDetailResp.getBannerIdList().add(adminBannerDetailResp.getId());
            }
            resp.getList().add(bannerPlanDetailResp);
        }
        return resp;
    }


}
