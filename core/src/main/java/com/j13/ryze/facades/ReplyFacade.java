package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.anno.NeedToken;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.AdminReplyAddReq;
import com.j13.ryze.api.req.ReplyAddReq;
import com.j13.ryze.api.req.ReplyListReq;
import com.j13.ryze.api.resp.AdminReplyAddResp;
import com.j13.ryze.api.resp.ReplyAddResp;
import com.j13.ryze.api.resp.ReplyDetailResp;
import com.j13.ryze.api.resp.ReplyListResp;
import com.j13.ryze.daos.PostDAO;
import com.j13.ryze.daos.ReplyDAO;
import com.j13.ryze.services.UserService;
import com.j13.ryze.vos.ReplyVO;
import com.j13.ryze.vos.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReplyFacade {

    @Autowired
    ReplyDAO replyDAO;
    @Autowired
    UserService userService;
    @Autowired
    PostDAO postDAO;

    @Action(name = "reply.list")
    public ReplyListResp list(CommandContext ctxt, ReplyListReq req) {
        ReplyListResp resp = new ReplyListResp();
        List<ReplyVO> list = replyDAO.list(req.getPostId(), req.getPageNum(), req.getSize());
        for (ReplyVO vo : list) {
            ReplyDetailResp r = new ReplyDetailResp();
            BeanUtils.copyProperties(r, vo);
            UserVO user = userService.getUserInfo(vo.getUserId());
            r.setUserName(user.getNickName());
            r.setUserAvatarUrl(user.getAvatarUrl());
            resp.getData().add(r);
            // 尝试找二级回复
            List<ReplyVO> list2 = replyDAO.lastReplylist(r.getReplyId(), req.getPageNum(), req.getSize());
            for (ReplyVO vo2 : list2) {
                ReplyDetailResp r2 = new ReplyDetailResp();
                BeanUtils.copyProperties(r2, vo2);
                UserVO user2 = userService.getUserInfo(vo2.getUserId());
                r2.setUserName(user2.getNickName());
                r2.setUserAvatarUrl(user2.getAvatarUrl());
                r.getReplyList().add(r2);
                // 尝试找第三级
                List<ReplyVO> list3 = replyDAO.lastReplylist(r2.getReplyId(), req.getPageNum(), req.getSize());
                for (ReplyVO vo3 : list3) {
                    ReplyDetailResp r3 = new ReplyDetailResp();
                    BeanUtils.copyProperties(r3, vo3);
                    UserVO user3 = userService.getUserInfo(vo3.getUserId());
                    r3.setUserName(user3.getNickName());
                    r3.setUserAvatarUrl(user3.getAvatarUrl());
                    r2.getReplyList().add(r3);
                }
            }
        }
        return resp;
    }


    @Action(name = "reply.add", desc = "")
    @NeedToken
    public ReplyAddResp replyAdd(CommandContext ctxt, ReplyAddReq req) {
        ReplyAddResp resp = new ReplyAddResp();
        int id = replyDAO.add(ctxt.getUid(), req.getBarId(), req.getPostId(),
                req.getContent(), req.getAnonymous(), req.getLastReplyId());
        postDAO.addReplyCount(req.getPostId());
        postDAO.updateTime(req.getPostId());
        resp.setReplyId(id);
        return resp;
    }

}
