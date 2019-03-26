package com.j13.ryze.facades;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Action;
import com.j13.poppy.anno.NeedToken;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.AdminReplyAddReq;
import com.j13.ryze.api.req.ReplyAddReq;
import com.j13.ryze.api.req.ReplyListReq;
import com.j13.ryze.api.resp.*;
import com.j13.ryze.daos.PostDAO;
import com.j13.ryze.daos.ReplyDAO;
import com.j13.ryze.services.UserService;
import com.j13.ryze.vos.ReplyVO;
import com.j13.ryze.vos.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
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
            // 获得一级评论的数据
            ReplyDetailResp r = new ReplyDetailResp();
            BeanUtils.copyProperties(r, vo);
            UserVO user = userService.getUserInfo(vo.getUserId());
            r.setUserName(user.getNickName());
            r.setUserAvatarUrl(user.getAvatarUrl());
            resp.getData().add(r);

            int replySize = 0;
            // 二级评论默认显示2个，其余显示一个总数，搜索的时候用这个参数作为size，
            // 组成集合之后通过updatetime排序之后按照这个参数取值
            int level2DefaultSize = 2;
            List<ReplyVO> tmpLevel2ReplyList = Lists.newLinkedList();

            // 尝试找二级回复
            List<ReplyVO> list2 = replyDAO.lastReplylist(r.getReplyId(), 0, level2DefaultSize);
            int list2Size = replyDAO.lastReplylistSize(r.getReplyId());
            replySize = replySize + list2Size;
            for (ReplyVO vo2 : list2) {
                tmpLevel2ReplyList.add(vo2);
                // 尝试找第三级
                List<ReplyVO> list3 = replyDAO.lastReplylist(vo2.getReplyId(), 0, level2DefaultSize);
                int list3Size = replyDAO.lastReplylistSize(vo2.getReplyId());
                replySize = replySize + list3Size;
                for (ReplyVO vo3 : list3) {
                    UserVO replyUser = userService.getUserInfo(vo2.getUserId());
                    vo3.setLastReplyUserName(replyUser.getNickName());
                    vo3.setLastReplyUserId(vo2.getUserId());
                    tmpLevel2ReplyList.add(vo3);
                }
            }

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
                UserVO replyUser = userService.getUserInfo(level2Resp.getUserId());
                level2Resp.setUserName(replyUser.getNickName());
                level2Resp.setUserAvatarUrl(replyUser.getAvatarUrl());
                r.getReplyList().add(level2Resp);
            }

            r.setReplySize(replySize);
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
