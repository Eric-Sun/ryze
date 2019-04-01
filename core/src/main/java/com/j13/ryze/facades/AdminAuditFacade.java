package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.AdminAuditReplyListReq;
import com.j13.ryze.api.resp.AdminPostListResp;
import com.j13.ryze.api.resp.AdminReplyDetailResp;
import com.j13.ryze.api.resp.AdminReplyListResp;
import com.j13.ryze.api.resp.ReplyDetailResp;
import com.j13.ryze.daos.PostDAO;
import com.j13.ryze.daos.ReplyDAO;
import com.j13.ryze.services.UserService;
import com.j13.ryze.vos.ReplyVO;
import com.j13.ryze.vos.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminAuditFacade {


    @Autowired
    ReplyDAO replyDAO;

    @Autowired
    UserService userService;

    @Action(name = "admin.audit.reply.list")
    public AdminReplyListResp replyList(CommandContext ctxt, AdminAuditReplyListReq req) {
        AdminReplyListResp resp = new AdminReplyListResp();

        List<ReplyVO> replyVOList = replyDAO.listByBarId(req.getBarId(), req.getPageNum(), req.getSize());
        for (ReplyVO vo : replyVOList) {
            AdminReplyDetailResp r = new AdminReplyDetailResp();
            UserVO userVO = userService.getUserInfo(vo.getUserId());
            BeanUtils.copyProperties(r, vo);
            r.setUserName(userVO.getNickName());
            resp.getData().add(r);
        }
        return resp;
    }
}
