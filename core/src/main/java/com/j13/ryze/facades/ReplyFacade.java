package com.j13.ryze.facades;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Action;
import com.j13.poppy.anno.NeedToken;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.*;
import com.j13.ryze.api.resp.*;
import com.j13.ryze.core.Constants;
import com.j13.ryze.daos.PostDAO;
import com.j13.ryze.daos.ReplyDAO;
import com.j13.ryze.services.AdminLevelInfoService;
import com.j13.ryze.services.UserService;
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

    @Action(name = "reply.list")
    public ReplyListResp list(CommandContext ctxt, ReplyListReq req) {
        ReplyListResp resp = new ReplyListResp();
        PostVO post = postDAO.get(req.getPostId());
        List<ReplyVO> list = replyDAO.list(req.getPostId(), req.getPageNum(), req.getSize());
        for (ReplyVO vo : list) {
            // 获得一级评论的数据
            ReplyDetailResp r = new ReplyDetailResp();
            BeanUtils.copyProperties(r, vo);
            userService.setUserInfoForReply(post.getAnonymous(), r, vo.getUserId());
            resp.getData().add(r);

            SizeObject replySize = new SizeObject();
            replySize.setSize(0);
            // 二级评论默认显示2个，其余显示一个总数，搜索的时候用这个参数作为size，
            // 组成集合之后通过updatetime排序之后按照这个参数取值
            int level2DefaultSize = 2;
            List<ReplyVO> tmpLevel2ReplyList = Lists.newLinkedList();

            findAllChildReply(post.getAnonymous(), vo.getReplyId(), level2DefaultSize, replySize, tmpLevel2ReplyList);


            Collections.sort(tmpLevel2ReplyList);
            List<ReplyVO> tmpFinalList = null;
            if (tmpLevel2ReplyList.size() >= 2) {
                tmpFinalList = tmpLevel2ReplyList.subList(0, level2DefaultSize);
            } else {
                tmpFinalList = tmpLevel2ReplyList;
            }


            for (ReplyVO finalVO : tmpFinalList) {
                Level2ReplyDetailResp level2Resp = new Level2ReplyDetailResp();
                BeanUtils.copyProperties(level2Resp, finalVO);
                userService.setUserInfoForReply(post.getAnonymous(), level2Resp, level2Resp.getUserId());
                r.getReplyList().add(level2Resp);
            }

            r.setReplySize(replySize.getSize());
        }
        return resp;
    }

    private void findAllChildReply(int postAnonymous, int replyId, int level2DefaultSize,
                                   SizeObject replySize, List<ReplyVO> tmpLevel2ReplyList) {
        List<ReplyVO> list = replyDAO.lastReplylist(replyId, 0, level2DefaultSize);
        int listSize = replyDAO.lastReplylistSize(replyId);
        replySize.setSize(replySize.getSize() + listSize);
        for (ReplyVO vo : list) {
            tmpLevel2ReplyList.add(vo);
            UserVO replyUser = userService.getUserInfo(vo.getUserId());
            vo.setLastReplyUserName(replyUser.getNickName());
            vo.setLastReplyUserId(vo.getUserId());
            if (vo.getAnonymous() == Constants.REPLY_ANONYMOUS.ANONYMOUS ||
                    postAnonymous == Constants.REPLY_ANONYMOUS.ANONYMOUS) {
                vo.setLastReplyUserName(replyUser.getAnonNickName());
            }

            findAllChildReply(postAnonymous, vo.getReplyId(), level2DefaultSize, replySize, tmpLevel2ReplyList);

        }
    }

    class SizeObject {
        private int size;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
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
        ReplyVO vo = replyDAO.get(req.getReplyId());
        PostVO post = postDAO.get(vo.getPostId());
        // 获得一级评论的数据
        ReplyDetailResp r = new ReplyDetailResp();
        BeanUtils.copyProperties(r, vo);
        userService.setUserInfoForReply(post.getAnonymous(), r, vo.getUserId());

        SizeObject replySize = new SizeObject();
        replySize.setSize(0);
        // 二级评论默认显示2个，其余显示一个总数，搜索的时候用这个参数作为size，
        // 组成集合之后通过updatetime排序之后按照这个参数取值
        int level2DefaultSize = 5;
        List<ReplyVO> tmpLevel2ReplyList = Lists.newLinkedList();

        findAllChildReply(post.getAnonymous(), vo.getReplyId(), level2DefaultSize, replySize, tmpLevel2ReplyList);

        Collections.sort(tmpLevel2ReplyList);

        for (ReplyVO finalVO : tmpLevel2ReplyList) {
            Level2ReplyDetailResp level2Resp = new Level2ReplyDetailResp();
            BeanUtils.copyProperties(level2Resp, finalVO);
            userService.setUserInfoForReply(post.getAnonymous(), level2Resp, level2Resp.getUserId());

            r.getReplyList().add(level2Resp);
        }
        r.setReplySize(replySize.getSize());


        return r;
    }

}
