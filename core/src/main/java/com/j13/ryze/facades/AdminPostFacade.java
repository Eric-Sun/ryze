package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.*;
import com.j13.ryze.api.resp.*;
import com.j13.ryze.daos.BarDAO;
import com.j13.ryze.daos.BarMemberDAO;
import com.j13.ryze.daos.PostDAO;
import com.j13.ryze.daos.UserDAO;
import com.j13.ryze.services.UserService;
import com.j13.ryze.vos.PostVO;
import com.j13.ryze.vos.UserVO;
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


    @Autowired
    UserService userService;

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

        int id = postDAO.add(req.getUserId(),
                req.getBarId(), req.getTitle(), req.getContent(), req.getAnonymous(), req.getType());
        barDAO.addPostCount(req.getBarId());
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

        String barName = barDAO.getBarName(req.getBarId());
        resp.setBarName(barName);
        List<PostVO> list = postDAO.list(req.getBarId(), req.getPageNum(), req.getSize());

        for (PostVO vo : list) {
            AdminPostDetailResp r = new AdminPostDetailResp();
            BeanUtils.copyProperties(r, vo);
            UserVO user = userService.getUserInfo(vo.getUserId());
            r.setUserName(user.getNickName());
            r.setUserAvatarUrl(user.getAvatarUrl());
            resp.getList().add(r);
        }
        return resp;
    }

    @Action(name = "admin.post.update")
    public CommonResultResp updateContentAndTitle(CommandContext ctxt, AdminPostUpdateContentReq req) {
        postDAO.update(req.getPostId(), req.getContent(), req.getTitle(), req.getAnonymous(), req.getType());
        return CommonResultResp.success();
    }


    @Action(name = "admin.post.delete")
    public CommonResultResp delete(CommandContext ctxt, AdminPostDeleteReq req) {
        postDAO.delete(req.getPostId());
        barDAO.reducePostCount(req.getBarId());
        return CommonResultResp.success();
    }

    @Action(name = "admin.post.detail")
    public AdminPostDetailResp detail(CommandContext ctxt, AdminPostDetailReq req) {
        AdminPostDetailResp resp = new AdminPostDetailResp();
        PostVO vo = postDAO.get(req.getPostId());
        BeanUtils.copyProperties(resp, vo);
        UserVO user = userService.getUserInfo(vo.getUserId());
        resp.setUserName(user.getNickName());
        resp.setUserAvatarUrl(user.getAvatarUrl());
        return resp;
    }

    @Action(name = "admin.post.offline")
    public CommonResultResp offline(CommandContext ctxt, AdminPostOfflineReq req) {
        postDAO.offline(req.getPostId());
        return CommonResultResp.success();
    }

    @Action(name = "admin.post.online")
    public CommonResultResp online(CommandContext ctxt, AdminPostOnlineReq req) {
        postDAO.online(req.getPostId());
        return CommonResultResp.success();
    }

    @Action(name = "admin.post.queryTitle")
    public AdminPostQueryTitleResp queryTitle(CommandContext ctxt, AdminPostQueryTitleReq req) {
        AdminPostQueryTitleResp resp = new AdminPostQueryTitleResp();
        List<PostVO> list = postDAO.queryByTtile(req.getBarId(), req.getName(), req.getPageNum(), req.getSize());
        for (PostVO vo : list) {
            AdminPostDetailResp r = new AdminPostDetailResp();
            BeanUtils.copyProperties(r, vo);
            UserVO user = userService.getUserInfo(vo.getUserId());
            r.setUserName(user.getNickName());
            r.setUserAvatarUrl(user.getAvatarUrl());
            resp.getList().add(r);
        }
        return resp;
    }

    @Action(name = "admin.post.queryUserId")
    public AdminPostQueryUserIdResp queryByUserId(CommandContext ctxt, AdminPostQueryUserIdReq req) {
        AdminPostQueryUserIdResp resp = new AdminPostQueryUserIdResp();
        List<PostVO> list = postDAO.queryByUserId(req.getBarId(), req.getUserId(), req.getPageNum(), req.getSize());
        for (PostVO vo : list) {
            AdminPostDetailResp r = new AdminPostDetailResp();
            BeanUtils.copyProperties(r, vo);
            UserVO user = userService.getUserInfo(vo.getUserId());
            r.setUserName(user.getNickName());
            r.setUserAvatarUrl(user.getAvatarUrl());
            resp.getList().add(r);
        }
        return resp;
    }

}
