package com.j13.garen.facade;

import com.j13.garen.api.req.BannerListReq;
import com.j13.garen.api.resp.BannerGetResp;
import com.j13.garen.api.resp.BannerListResp;
import com.j13.garen.api.resp.ItemGetResp;
import com.j13.garen.daos.BannerDAO;
import com.j13.garen.vos.BannerVO;
import com.j13.garen.vos.ItemVO;
import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BannerFacade {


    @Autowired
    BannerDAO bannerDAO;


    @Action(name = "banner.list", desc = "banner list.")
    public BannerListResp list(CommandContext cmd, BannerListReq req){
        BannerListResp resp = new BannerListResp();
        List<BannerVO> list = bannerDAO.list();
        for (BannerVO vo : list) {
            BannerGetResp r = new BannerGetResp();
            BeanUtils.copyProperties(r, vo);
            resp.getList().add(r);
        }
        return resp;

    }


}
