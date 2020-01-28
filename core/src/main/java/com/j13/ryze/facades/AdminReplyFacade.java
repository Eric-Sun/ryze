package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.*;
import com.j13.ryze.api.resp.AdminLevelInfoResp;
import com.j13.ryze.api.resp.AdminReplyAddResp;
import com.j13.ryze.api.resp.AdminReplyDetailResp;
import com.j13.ryze.api.resp.AdminReplyListResp;
import com.j13.ryze.daos.PostDAO;
import com.j13.ryze.daos.ReplyDAO;
import com.j13.ryze.daos.UserDAO;
import com.j13.ryze.services.AdminLevelInfoService;
import com.j13.ryze.services.ReplyService;
import com.j13.ryze.services.UserService;
import com.j13.ryze.vos.ReplyVO;
import com.j13.ryze.vos.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminReplyFacade {

    @Autowired
    ReplyDAO replyDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    PostDAO postDAO;
    @Autowired
    UserService userService;
    @Autowired
    AdminLevelInfoService adminLevelInfoService;
    @Autowired
    ReplyService replyService;

    @Action(name = "admin.reply.add", desc = "")
    public AdminReplyAddResp replyAdd(CommandContext ctxt, AdminReplyAddReq req) {
        AdminReplyAddResp resp = new AdminReplyAddResp();
        int id = replyDAO.add(req.getUserId(), req.getBarId(), req.getPostId(),
                req.getContent(), req.getAnonymous(), req.getLastReplyId(), req.getImgListStr());
        postDAO.addReplyCount(req.getPostId());
        postDAO.updateTime(req.getPostId());
        resp.setReplyId(id);
        return resp;
    }

    @Action(name = "admin.reply.list")
    public AdminReplyListResp replyList(CommandContext ctxt, AdminReplyListReq req) {
        AdminReplyListResp resp = new AdminReplyListResp();
        List<ReplyVO> list = replyDAO.list(req.getPostId(), req.getPageNum(), req.getSize());
        int count = replyDAO.count(req.getPostId());
        for (ReplyVO vo : list) {
            AdminReplyDetailResp r = new AdminReplyDetailResp();
            BeanUtils.copyProperties(r, vo);
            // parse imgList
            replyService.parseImgList(vo);
            UserVO user = userService.getUserInfo(vo.getUserId());
            r.setUserName(user.getNickName());
            r.setUserAvatarUrl(user.getAvatarUrl());
            resp.getData().add(r);

            int replyListSize = replyDAO.lastReplylistSize(r.getReplyId());
            r.setReplyListSize(replyListSize);
        }
        resp.setCount(count);
        return resp;
    }


    @Action(name = "admin.reply.detail")
    public AdminReplyDetailResp detail(CommandContext ctxt, AdminReplyDetailReq req) {
        ReplyVO vo = replyDAO.get(req.getReplyId());
        AdminReplyDetailResp r = new AdminReplyDetailResp();
        BeanUtils.copyProperties(r, vo);
        UserVO user1 = userService.getUserInfo(vo.getUserId());
        r.setUserName(user1.getNickName());
        r.setUserAvatarUrl(user1.getAvatarUrl());
        replyService.parseImgList(vo);

        List<ReplyVO> replyList = replyDAO.lastReplylist(req.getReplyId(), req.getPageNum(), req.getSize());
        int count = replyDAO.lastReplylistSize(req.getReplyId());
        for (ReplyVO replyVO : replyList) {
            AdminReplyDetailResp r2 = new AdminReplyDetailResp();
            BeanUtils.copyProperties(r2, replyVO);
            UserVO user2 = userService.getUserInfo(replyVO.getUserId());
            r2.setUserName(user2.getNickName());
            replyService.parseImgList(replyVO);

            int replyListSize = replyDAO.lastReplylistSize(r2.getReplyId());
            r2.setReplyListSize(replyListSize);
            r.getReplyList().add(r2);

        }
        List<AdminLevelInfoResp> levelInfoList = adminLevelInfoService.findLevelInfo(vo.getReplyId(), 2);
        r.setLevelInfo(levelInfoList);
        r.setReplyListSize(count);
        return r;
    }

    @Action(name = "admin.reply.update")
    public CommonResultResp update(CommandContext ctxt, AdminReplyUpdateReq req) {
        replyDAO.update(req.getReplyId(), req.getContent(), req.getAnonymous(), req.getImgListStr());
        return CommonResultResp.success();
    }

    @Action(name = "admin.reply.delete")
    public CommonResultResp replyDelete(CommandContext ctxt, AdminReplyDeleteReq req) {
        replyDAO.delete(req.getReplyId());
        postDAO.reduceReplyCount(req.getPostId());
        return CommonResultResp.success();
    }


}
