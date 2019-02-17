package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.AdminReplyAddReq;
import com.j13.ryze.api.req.AdminReplyDeleteReq;
import com.j13.ryze.api.req.AdminReplyListReq;
import com.j13.ryze.api.req.AdminReplyUpdateContentReq;
import com.j13.ryze.api.resp.AdminReplyAddResp;
import com.j13.ryze.api.resp.AdminReplyDetailResp;
import com.j13.ryze.api.resp.AdminReplyListResp;
import com.j13.ryze.daos.ReplyDAO;
import com.j13.ryze.vos.ReplyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminReplyFacade {

    @Autowired
    ReplyDAO replyDAO;


    @Action(name = "admin.reply.add", desc = "")
    public AdminReplyAddResp replyAdd(CommandContext ctxt, AdminReplyAddReq req) {
        AdminReplyAddResp resp = new AdminReplyAddResp();
        int id = replyDAO.add(req.getUserId(), req.getBarId(), req.getPostId(), req.getContent());
        resp.setReplyId(id);
        return resp;
    }


    @Action(name = "admin.reply.list")
    public AdminReplyListResp replyList(CommandContext ctxt, AdminReplyListReq req) {
        AdminReplyListResp resp = new AdminReplyListResp();
        List<ReplyVO> list = replyDAO.list(req.getBarId(), req.getPostId(), req.getPageNum(), req.getSize());
        for (ReplyVO vo : list) {
            AdminReplyDetailResp r = new AdminReplyDetailResp();
            BeanUtils.copyProperties(r, vo);
            resp.getData().add(r);
        }
        return resp;
    }

    @Action(name = "admin.reply.updateContent")
    public CommonResultResp replyUpdateContent(CommandContext ctxt, AdminReplyUpdateContentReq req) {
        replyDAO.updateContent(req.getReplyId(), req.getContent());
        return CommonResultResp.success();
    }

    @Action(name = "admin.reply.delete")
    public CommonResultResp replyDelete(CommandContext ctxt, AdminReplyDeleteReq req) {
        replyDAO.delete(req.getReplyId());
        return CommonResultResp.success();
    }


}
