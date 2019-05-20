package com.j13.ryze.facades;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Action;
import com.j13.poppy.anno.NeedToken;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.exceptions.CommonException;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.*;
import com.j13.ryze.api.resp.*;
import com.j13.ryze.core.Constants;
import com.j13.ryze.core.ErrorCode;
import com.j13.ryze.daos.PostDAO;
import com.j13.ryze.daos.ReplyDAO;
import com.j13.ryze.services.*;
import com.j13.ryze.vos.PostVO;
import com.j13.ryze.vos.ReplyVO;
import com.j13.ryze.vos.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ReplyFacade {

    private static Logger LOG = LoggerFactory.getLogger(ReplyFacade.class);

    @Autowired
    ReplyDAO replyDAO;
    @Autowired
    UserService userService;
    @Autowired
    PostDAO postDAO;
    @Autowired
    AdminLevelInfoService adminLevelInfoService;
    @Autowired
    ReplyService replyService;
    @Autowired
    PostService postService;
    @Autowired
    NoticeService noticeService;
    @Autowired
    IAcsClientService iAcsClientService;

    @Action(name = "reply.list")
    public ReplyListResp list(CommandContext ctxt, ReplyListReq req) {
        ReplyListResp resp = new ReplyListResp();
        PostVO post = postDAO.get(req.getPostId());
        List<ReplyVO> list = replyDAO.list(req.getPostId(), req.getPageNum(), req.getSize());
        replyService.handleReplyList(post, list, resp);
        return resp;
    }


    @Action(name = "reply.reverseList")
    public ReplyListResp reverseList(CommandContext ctxt, ReplyListReq req) {
        ReplyListResp resp = new ReplyListResp();
        PostVO post = postDAO.get(req.getPostId());
        List<ReplyVO> list = replyDAO.reverselist(req.getPostId(), req.getPageNum(), req.getSize());
        replyService.handleReplyList(post, list, resp);
        return resp;
    }


    @Action(name = "reply.add", desc = "")
    @NeedToken
    public ReplyAddResp replyAdd(CommandContext ctxt, ReplyAddReq req) {

        boolean b = iAcsClientService.scan(req.getContent());
        if (b == false) {
            throw new CommonException(ErrorCode.Common.CONTENT_ILLEGAL);
        }

        ReplyAddResp resp = new ReplyAddResp();
        int id = replyDAO.add(ctxt.getUid(), req.getBarId(), req.getPostId(),
                req.getContent(), req.getAnonymous(), req.getLastReplyId(), req.getImgListStr());
        postDAO.addReplyCount(req.getPostId());
        postDAO.updateTime(req.getPostId());
        // 添加对应的notice通知
        if (req.getLastReplyId() == 0) {
            PostVO postVO = postService.getSimplePost(req.getPostId());
            noticeService.addPostNotice(ctxt.getUid(), postVO.getUserId(), postVO.getPostId(), id);
        } else {
            ReplyVO replyVO = replyService.getSimpleReply(req.getLastReplyId());
            noticeService.addReplyNotice(ctxt.getUid(), replyVO.getUserId(), replyVO.getReplyId(), id);
        }
        resp.setReplyId(id);
        return resp;
    }

    @Action(name = "reply.delete")
    @NeedToken
    public CommonResultResp delete(CommandContext ctxt, ReplyDeleteReq req) {
        int userId = ctxt.getUid();
        replyDAO.delete(userId, req.getReplyId());
        LOG.info("delete reply . replyId={},userId={}", req.getReplyId(), userId);
        return CommonResultResp.success();
    }

    @Action(name = "reply.detail")
    public ReplyDetailResp detail(CommandContext ctxt, ReplyDetailReq req) {
        return replyService.handleReplyDetail(req.getReplyId());
    }

}
