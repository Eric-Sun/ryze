package com.j13.ryze.facades;

import com.j13.poppy.core.CommandContext;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.NoticeListReq;
import com.j13.ryze.api.resp.NoticeDetailResp;
import com.j13.ryze.api.resp.NoticeListResp;
import com.j13.ryze.api.resp.NoticePostContentResp;
import com.j13.ryze.api.resp.NoticeReplyContentResp;
import com.j13.ryze.core.Constants;
import com.j13.ryze.services.NoticeService;
import com.j13.ryze.services.PostService;
import com.j13.ryze.services.ReplyService;
import com.j13.ryze.services.UserService;
import com.j13.ryze.vos.NoticeVO;
import com.j13.ryze.vos.PostVO;
import com.j13.ryze.vos.ReplyVO;
import com.j13.ryze.vos.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoticeFacade {


    @Autowired
    NoticeService noticeService;
    @Autowired
    PostService postService;
    @Autowired
    UserService userService;
    @Autowired
    ReplyService replyService;

    public NoticeListResp list(CommandContext ctxt, NoticeListReq req) {
        NoticeListResp resp = new NoticeListResp();
        List<NoticeVO> list = null;
        if (req.getStatus() == -1) {
            // 获取全部，按照时间正序混排
            list = noticeService.list(ctxt.getUid());
        }

        for (NoticeVO vo : list) {
            NoticeDetailResp detailResp = new NoticeDetailResp();


            if (vo.getType() == Constants.NOTICE.TYPE.POST_NOTICE) {
                // post notice
                PostVO postVO = postService.getSimplePost(vo.getTargetResourceId());
                UserVO userVO = userService.getUserInfo(vo.getFromUserId());

                BeanUtils.copyProperties(detailResp, vo);
                if (postVO.getAnonymous() == Constants.POST_ANONYMOUS.ANONYMOUS) {
                    detailResp.setFromUserAvatarImgUrl(userVO.getAvatarUrl());
                    detailResp.setFromUserNickName(userVO.getNickName());
                } else {
                    detailResp.setFromUserAvatarImgUrl(userVO.getAnonXiaUrl());
                    detailResp.setFromUserNickName(userVO.getAnonNickName());
                }

                NoticePostContentResp postContent = new NoticePostContentResp();
                postContent.setPostId(postVO.getPostId());
                postContent.setPostTitle(postVO.getTitle());
                UserVO postUserVO = userService.getUserInfo(postVO.getUserId());

                if (postVO.getAnonymous() == Constants.POST_ANONYMOUS.ANONYMOUS) {
                    postContent.setPostUserAvatarImgUrl(postUserVO.getAvatarUrl());
                    postContent.setPostUserNickName(postUserVO.getNickName());
                } else {
                    postContent.setPostUserAvatarImgUrl(postUserVO.getAnonLouUrl());
                    postContent.setPostUserNickName(postUserVO.getAnonNickName());
                }
                postContent.setPostUserId(postVO.getUserId());
                detailResp.setContent(postContent);
            } else {
                // reply notice
                NoticeReplyContentResp replyContent = new NoticeReplyContentResp();
                ReplyVO replyVO = replyService.getSimpleReply(vo.getTargetResourceId());
                PostVO postVO = postService.getSimplePost(replyVO.getPostId());

                replyContent.setPostId(postVO.getPostId());
                replyContent.setPostTitle(postVO.getTitle());
                UserVO postUserVO = userService.getUserInfo(postVO.getUserId());

                if (postVO.getAnonymous() == Constants.POST_ANONYMOUS.ANONYMOUS) {
                    replyContent.setPostUserAvatarImgUrl(postUserVO.getAvatarUrl());
                    replyContent.setPostUserNickName(postUserVO.getNickName());
                } else {
                    replyContent.setPostUserAvatarImgUrl(postUserVO.getAnonLouUrl());
                    replyContent.setPostUserNickName(postUserVO.getAnonNickName());
                }
                replyContent.setPostUserId(postVO.getUserId());

                // add reply info
                replyContent.setTargetReplyId(replyVO.getReplyId());
                replyContent.setTargetReplyUserId(replyVO.getUserId());
                UserVO replyUserVO = userService.getUserInfo(replyVO.getUserId());

                if (postVO.getAnonymous() != Constants.POST_ANONYMOUS.ANONYMOUS
                        && replyVO.getAnonymous() != Constants.POST_ANONYMOUS.ANONYMOUS) {
                    replyContent.setTargetReplyUserAvatarImgUrl(replyUserVO.getAnonXiaUrl());
                    replyContent.setTargetReplyUserNickName(replyUserVO.getAnonNickName());
                } else {
                    replyContent.setTargetReplyUserAvatarImgUrl(replyUserVO.getAvatarUrl());
                    replyContent.setTargetReplyUserNickName(replyUserVO.getNickName());
                }

                replyContent.setTargetReplyContent(replyVO.getContent());
                if (replyVO.getLastReplyId() != 0) {
                    ReplyVO level2ReplyVO = replyService.getSimpleReply(replyVO.getLastReplyId());
                    UserVO level2ReplyUserVO = userService.getUserInfo(level2ReplyVO.getUserId());
                    replyContent.setTargetReplyRepliedUserId(level2ReplyVO.getUserId());
                    if (postVO.getAnonymous() != Constants.POST_ANONYMOUS.ANONYMOUS
                            && level2ReplyVO.getAnonymous() != Constants.POST_ANONYMOUS.ANONYMOUS) {
                        replyContent.setTargetReplyRepliedUserAvatarImgUrl(level2ReplyUserVO.getAnonXiaUrl());
                        replyContent.setTargetReplyRepliedUserNickName(level2ReplyUserVO.getAnonNickName());
                    } else {
                        replyContent.setTargetReplyRepliedUserAvatarImgUrl(level2ReplyUserVO.getAvatarUrl());
                        replyContent.setTargetReplyRepliedUserNickName(level2ReplyUserVO.getNickName());
                    }

                }
                detailResp.setContent(replyContent);
            }
            resp.getList().add(detailResp);
        }

        return resp;
    }
}
