package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.*;
import com.j13.ryze.api.resp.AdminReplyAddResp;
import com.j13.ryze.api.resp.AdminReplyDetailResp;
import com.j13.ryze.api.resp.AdminReplyListResp;
import com.j13.ryze.daos.PostDAO;
import com.j13.ryze.daos.ReplyDAO;
import com.j13.ryze.daos.UserDAO;
import com.j13.ryze.vos.ReplyVO;
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

    @Action(name = "admin.reply.add", desc = "")
    public AdminReplyAddResp replyAdd(CommandContext ctxt, AdminReplyAddReq req) {
        AdminReplyAddResp resp = new AdminReplyAddResp();
        int id = replyDAO.add(req.getUserId(), req.getBarId(), req.getPostId(),
                req.getContent(), req.getAnonymous(),req.getLastReplyId());
        postDAO.addReplyCount(req.getPostId());
        postDAO.updateTime(req.getPostId());
        resp.setReplyId(id);
        return resp;
    }


    @Action(name = "admin.reply.list")
    public AdminReplyListResp replyList(CommandContext ctxt, AdminReplyListReq req) {
        AdminReplyListResp resp = new AdminReplyListResp();
        List<ReplyVO> list = replyDAO.list(req.getPostId(), req.getPageNum(), req.getSize());
        for (ReplyVO vo : list) {
            AdminReplyDetailResp r = new AdminReplyDetailResp();
            BeanUtils.copyProperties(r, vo);
            r.setUserName(userDAO.getNickName(vo.getUserId()));
            resp.getData().add(r);
            // 尝试找二级回复
            List<ReplyVO> list2 = replyDAO.lastReplylist(r.getReplyId(), req.getPageNum(), req.getSize());
            for (ReplyVO vo2 : list2) {
                AdminReplyDetailResp r2 = new AdminReplyDetailResp();
                BeanUtils.copyProperties(r2, vo2);
                r2.setUserName(userDAO.getNickName(vo2.getUserId()));
                r.getReplyList().add(r2);
                // 尝试找第三级
                List<ReplyVO> list3 = replyDAO.lastReplylist(r2.getReplyId(), req.getPageNum(), req.getSize());
                for (ReplyVO vo3 : list3) {
                    AdminReplyDetailResp r3 = new AdminReplyDetailResp();
                    BeanUtils.copyProperties(r3, vo3);
                    r3.setUserName(userDAO.getNickName(vo3.getUserId()));
                    r2.getReplyList().add(r3);
                }
            }
        }
        return resp;
    }

    @Action(name = "admin.reply.detail")
    public AdminReplyDetailResp detail(CommandContext ctxt, AdminReplyDetailReq req) {
        ReplyVO vo = replyDAO.get(req.getReplyId());
        AdminReplyDetailResp r = new AdminReplyDetailResp();
        BeanUtils.copyProperties(r, vo);
        r.setUserName(userDAO.getNickName(vo.getUserId()));
        // 尝试找二级回复
        List<ReplyVO> list2 = replyDAO.lastReplylist(r.getReplyId(), 0, 500);
        for (ReplyVO vo2 : list2) {
            AdminReplyDetailResp r2 = new AdminReplyDetailResp();
            BeanUtils.copyProperties(r2, vo2);
            r2.setUserName(userDAO.getNickName(vo2.getUserId()));
            r.getReplyList().add(r2);
            // 尝试找第三级
            List<ReplyVO> list3 = replyDAO.lastReplylist(r2.getReplyId(), 0, 500);
            for (ReplyVO vo3 : list3) {
                AdminReplyDetailResp r3 = new AdminReplyDetailResp();
                BeanUtils.copyProperties(r3, vo3);
                r3.setUserName(userDAO.getNickName(vo3.getUserId()));
                r2.getReplyList().add(r3);
            }
        }
        return r;
    }

    @Action(name = "admin.reply.update")
    public CommonResultResp update(CommandContext ctxt, AdminReplyUpdateReq req) {
        replyDAO.update(req.getReplyId(), req.getContent(), req.getAnonymous());
        return CommonResultResp.success();
    }

    @Action(name = "admin.reply.delete")
    public CommonResultResp replyDelete(CommandContext ctxt, AdminReplyDeleteReq req) {
        replyDAO.delete(req.getReplyId());
        postDAO.reduceReplyCount(req.getPostId());
        return CommonResultResp.success();
    }


}
