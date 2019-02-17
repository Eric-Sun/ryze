package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.exceptions.CommonException;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.AdminPostAddReq;
import com.j13.ryze.api.req.AdminPostListReq;
import com.j13.ryze.api.req.AdminPostDeleteReq;
import com.j13.ryze.api.req.AdminPostUpdateContentReq;
import com.j13.ryze.api.resp.AdminPostAddResp;
import com.j13.ryze.api.resp.AdminPostListResp;
import com.j13.ryze.api.resp.AdminPostDetailResp;
import com.j13.ryze.core.ErrorCode;
import com.j13.ryze.daos.BarDAO;
import com.j13.ryze.daos.BarMemberDAO;
import com.j13.ryze.daos.PostDAO;
import com.j13.ryze.vos.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminPostFacade {

    @Autowired
    PostDAO postDAO;

    @Autowired
    BarDAO barDAO;

    @Autowired
    BarMemberDAO barMemberDAO;

    @Action(name = "admin.post.add", desc = "")
    public AdminPostAddResp add(CommandContext ctxt, AdminPostAddReq req) {
        AdminPostAddResp resp = new AdminPostAddResp();
//        if (!barDAO.exist(req.getBarId())) {
//            throw new CommonException(ErrorCode.Bar.NOT_EXIST);
//        }
//
//        if (!barMemberDAO.hasMember(req.getBarId(), req.getUserId())) {
//            throw new CommonException(ErrorCode.Bar.NOT_HAS_MEMBER);
//        }

        int id = postDAO.add(req.getUserId(), req.getBarId(), req.getContent());
        resp.setPostId(id);
        return resp;
    }

    @Action(name = "admin.post.list", desc = "")
    public AdminPostListResp list(CommandContext ctxt, AdminPostListReq req) {
        AdminPostListResp resp = new AdminPostListResp();
//        if (!barDAO.exist(req.getBarId())) {
//            throw new CommonException(ErrorCode.Bar.NOT_EXIST);
//        }
//        if (!barMemberDAO.hasMember(req.getBarId(), req.getUserId())) {
//            throw new CommonException(ErrorCode.Bar.NOT_HAS_MEMBER);
//        }

        List<PostVO> list = postDAO.list(req.getBarId(), req.getPageNum(), req.getSize());

        for (PostVO vo : list) {
            AdminPostDetailResp r = new AdminPostDetailResp();
            BeanUtils.copyProperties(r, vo);
            resp.getList().add(r);
        }
        return resp;
    }

    @Action(name = "admin.post.updateContent")
    public CommonResultResp updateContent(CommandContext ctxt, AdminPostUpdateContentReq req) {
        postDAO.updateContent(req.getBarId(), req.getPostId(), req.getContent());
        return CommonResultResp.success();
    }


    @Action(name = "admin.post.delete")
    public CommonResultResp delete(CommandContext ctxt, AdminPostDeleteReq req) {
        postDAO.delete(req.getPostId());
        return CommonResultResp.success();
    }
}
