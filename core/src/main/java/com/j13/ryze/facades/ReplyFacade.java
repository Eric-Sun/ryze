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
    public ReplyListResp list(CommandContext ctxt, ReplyListReq req) {
        ReplyListResp resp = new ReplyListResp();
        PostCursorDetailResp cursorResp = new PostCursorDetailResp();
        List<ReplyVO> replyList = null;
        if (req.getPageNum() == -1) {
            PostCursorVO postCursorVO = postCursorService.getCursor(ctxt.getUid(), req.getPostId());
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
            noticeService.addPostNotice(ctxt.getUid(), postVO.getUserId(), postVO.getPostId(), replyId);
        } else {
            ReplyVO replyVO = replyService.getSimpleReply(req.getLastReplyId());
            noticeService.addReplyNotice(ctxt.getUid(), replyVO.getUserId(), replyVO.getReplyId(), replyId);
        }

        // 当是楼主回帖的时候才会触发收藏帖子通知的机制
        if (postVO.getUserId() == ctxt.getUid()) {
            // 添加收藏了这个帖子的所有用户发通知
            List<CollectionVO> collectionVOList = collectionService.queryCollectionsByResourceId(req.getPostId(), Constants.Collection.Type.POST);
            for (CollectionVO vo : collectionVOList) {
                // 检查是否已经有关于这个帖子和用户的未读通知，如果有的话就不插入了
                int noticeId = noticeService.checkPostCollectionNoticeExist(vo.getUserId(), req.getPostId());
                if (noticeId == 0) {
                    // 需要插入这个通知
                    noticeService.addPostCollctionNotice(vo.getUserId(), req.getPostId());
                } else {
                    // 如果已经存在需要更新时间
                    noticeService.updateUpdateTime(noticeId);
                }
            }
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
        ReplyVO vo = replyService.handleReplyDetail(req.getReplyId());
        BeanUtils.copyProperties(resp, vo);
        return resp;
    }

}
