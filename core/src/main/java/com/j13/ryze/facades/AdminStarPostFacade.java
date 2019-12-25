package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.ryze.api.req.AdminStarPostAddReq;
import com.j13.ryze.api.req.AdminStarPostDeleteReq;
import com.j13.ryze.api.req.AdminStarPostListReq;
import com.j13.ryze.api.resp.AdminStarPostAddResp;
import com.j13.ryze.api.resp.AdminStarPostDetailResp;
import com.j13.ryze.api.resp.AdminStarPostListResp;
import com.j13.ryze.daos.StarPostDAO;
import com.j13.ryze.services.PostService;
import com.j13.ryze.vos.PostVO;
import com.j13.ryze.vos.StarPostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminStarPostFacade {

    @Autowired
    StarPostDAO starPostDAO;

    @Autowired
    PostService postService;


    @Action(name = "admin.starPost.add")
    public AdminStarPostAddResp add(CommandContext ctxt, AdminStarPostAddReq req) {
        int id = starPostDAO.add(req.getPostId(), req.getValue());
        AdminStarPostAddResp resp = new AdminStarPostAddResp();
        resp.setId(id);
        return resp;
    }

    @Action(name = "admin.starPost.delete")
    public CommonResultResp delete(CommandContext ctxt, AdminStarPostDeleteReq req) {
        starPostDAO.delete(req.getId());
        return CommonResultResp.success();
    }


    @Action(name = "admin.starPost.list")
    public AdminStarPostListResp list(CommandContext ctxt, AdminStarPostListReq req) {
        AdminStarPostListResp resp = new AdminStarPostListResp();
        List<StarPostVO> voList = starPostDAO.list();
        for (StarPostVO vo : voList) {
            AdminStarPostDetailResp detailResp = new AdminStarPostDetailResp();
            detailResp.setId(vo.getId());
            detailResp.setPostId(vo.getPostId());
            detailResp.setValue(vo.getValue());
            PostVO postVO = postService.getSimplePost(vo.getPostId());
            detailResp.setTitle(postVO.getTitle());
            resp.getData().add(detailResp);
        }
        return resp;
    }


}
