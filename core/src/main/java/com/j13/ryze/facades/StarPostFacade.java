package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.ryze.api.req.StarPostAddReq;
import com.j13.ryze.api.req.StarPostDeleteReq;
import com.j13.ryze.api.req.StarPostListReq;
import com.j13.ryze.api.resp.StarPostAddResp;
import com.j13.ryze.api.resp.StarPostDetailResp;
import com.j13.ryze.api.resp.StarPostListResp;
import com.j13.ryze.daos.StarPostDAO;
import com.j13.ryze.services.PostService;
import com.j13.ryze.vos.PostVO;
import com.j13.ryze.vos.StarPostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StarPostFacade {

    @Autowired
    StarPostDAO starPostDAO;

    @Autowired
    PostService postService;


    @Action(name = "starPost.add")
    public StarPostAddResp add(CommandContext ctxt, StarPostAddReq req) {
        int id = starPostDAO.add(req.getPostId(), req.getValue());
        StarPostAddResp resp = new StarPostAddResp();
        resp.setId(id);
        return resp;
    }

    @Action(name = "starPost.delete")
    public CommonResultResp delete(CommandContext ctxt, StarPostDeleteReq req) {
        starPostDAO.delete(req.getId());
        return CommonResultResp.success();
    }


    @Action(name = "starPost.list")
    public StarPostListResp list(CommandContext ctxt, StarPostListReq req) {
        StarPostListResp resp = new StarPostListResp();
        List<StarPostVO> voList = starPostDAO.list();
        for (StarPostVO vo : voList) {
            StarPostDetailResp detailResp = new StarPostDetailResp();
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
