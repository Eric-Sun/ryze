package com.j13.ryze.facades;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Action;
import com.j13.poppy.anno.NeedToken;
import com.j13.poppy.anno.TokenExpireDontThrow16;
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
import com.j13.ryze.vos.*;
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
    @Autowired
    CollectionService collectionService;
    @Autowired
    PostCursorService postCursorService;

    @Action(name = "reply.list")
    @NeedToken
    @TokenExpireDontThrow16
    public ReplyListResp list(CommandContext ctxt, ReplyListReq req) {
        ReplyListResp resp = new ReplyListResp();
        PostCursorDetailResp cursorResp = new PostCursorDetailResp();
        PostVO postVO = postService.getSimplePost(req.getPostId());
        List<ReplyVO> replyList = null;
        if (req.getPageNum() == -1) {
            // 意味着从来没有看过这篇文章，需要获取cursor
            PostCursorVO postCursorVO = postCursorService.getCursor(req.getUserToken(), req.getPostId());
            BeanUtils.copyProperties(cursorResp, postCursorVO);
            resp.setCursorInfo(cursorResp);
            replyList = replyService.list(req.getPostId(), postCursorVO.getPageNum(), Constants.Reply.REPLY_SIZE_PER_PAGE);
        } else {
            PostCursorVO postCursorVO = new PostCursorVO();
            postCursorVO.setPageNum(req.getPageNum());
            postCursorVO.setCursor(0);
            BeanUtils.copyProperties(cursorResp, postCursorVO);
            resp.setCursorInfo(cursorResp);
            replyList = replyService.list(req.getPostId(), req.getPageNum(), Constants.Reply.REPLY_SIZE_PER_PAGE);
        }

        for (ReplyVO vo : replyList) {
            ReplyDetailResp detailResp = new ReplyDetailResp();
            if (postVO.getUserId() != vo.getUserId()) {
                //替换抓取的全角空格
                vo.setContent(vo.getContent().replace((char) 12288, '\0'));
            }
            BeanUtils.copyProperties(detailResp, vo);
            resp.getData().add(detailResp);
        }


        return resp;
    }


    @Action(name = "reply.reverseList")
    public ReplyListResp reverseList(CommandContext ctxt, ReplyListReq req) {
        ReplyListResp resp = new ReplyListResp();
//        PostVO post = postDAO.get(req.getPostId());
//        List<ReplyVO> list = replyDAO.reverselist(req.getPostId(), req.getPageNum(), req.getSize());
//        replyService.handleReplyList(post, list, resp);
        return resp;
    }


    @Action(name = "reply.add", desc = "")
    @NeedToken
    public ReplyAddResp replyAdd(CommandContext ctxt, ReplyAddReq req) {
        ReplyAddResp resp = new ReplyAddResp();
        int replyId = replyService.add(ctxt.getUid(), req.getBarId(), req.getPostId(), req.getContent(),
                req.getAnonymous(), req.getLastReplyId(), req.getImgListStr(), true);

        PostVO postVO = postService.getSimplePost(req.getPostId());

        replyService.updateReplyListCache(postVO.getPostId());
        // 添加对应的notice通知
        if (req.getLastReplyId() == 0) {
            // 当出现一级回复的时候，给发帖人发通知，为Post通知
            noticeService.addPostNotice(ctxt.getUid(), postVO.getUserId(), postVO.getPostId(), replyId);
        } else {
            // 当出现二级回复的时候，给回复的人发通知，为reply通知
            ReplyVO replyVO = replyService.getSimpleReply(req.getLastReplyId());
            noticeService.addReplyNotice(ctxt.getUid(), replyVO.getUserId(), replyVO.getReplyId(), replyId);
        }

        if (postVO.getUserId() == ctxt.getUid()) {
            // 如果发帖人发新的内容了，则给所有收藏这个帖子的人发消息
            noticeService.sendPostNotices(req.getPostId());
        }

        resp.setReplyId(replyId);
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
        ReplyDetailResp resp = new ReplyDetailResp();
        ReplyVO vo = replyService.handleReplyDetail(req.getReplyId(), req.getPageNum(), req.getSize());
        BeanUtils.copyProperties(resp, vo);
        return resp;
    }

}
