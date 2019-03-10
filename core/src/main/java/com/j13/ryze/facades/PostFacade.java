package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.PostDetailResp;
import com.j13.ryze.api.req.PostListResp;
import com.j13.ryze.api.resp.PostListReq;
import com.j13.ryze.daos.PostDAO;
import com.j13.ryze.daos.UserDAO;
import com.j13.ryze.vos.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostFacade {

    @Autowired
    PostDAO postDAO;

    @Autowired
    UserDAO userDAO;

    @Action(name = "post.list", desc = "")
    public PostListResp list(CommandContext ctxt, PostListReq req) {
        PostListResp resp = new PostListResp();
        List<PostVO> list = postDAO.list(req.getBarId(), req.getPageNum(), req.getSize());
        for (PostVO vo : list) {
            PostDetailResp r = new PostDetailResp();
            BeanUtils.copyProperties(r, vo);
            r.setUserName(userDAO.getNickName(r.getUserId()));
            resp.getList().add(r);
        }
        return resp;
    }



}
