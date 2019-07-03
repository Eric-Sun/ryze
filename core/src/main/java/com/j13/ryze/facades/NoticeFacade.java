package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.anno.NeedToken;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.NoticeListReq;
import com.j13.ryze.api.req.NoticeReadAllReq;
import com.j13.ryze.api.req.PostDetailResp;
import com.j13.ryze.api.resp.*;
import com.j13.ryze.core.Constants;
import com.j13.ryze.services.NoticeService;
import com.j13.ryze.services.PostService;
import com.j13.ryze.services.ReplyService;
import com.j13.ryze.services.UserService;
import com.j13.ryze.vos.*;
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

    @Action(name = "notice.list")
    @NeedToken
    public NoticeListResp list(CommandContext ctxt, NoticeListReq req) {
        NoticeListResp resp = new NoticeListResp();
        List<NoticeVO> list = null;
        // 获取全部，按照时间正序混排
        list = noticeService.list(ctxt.getUid());

        for (NoticeVO vo : list) {
            NoticeDetailResp detailResp = new NoticeDetailResp();

            if (vo.getType() == Constants.NOTICE.TYPE.POST_NOTICE) {
                ReplyVO triggerReplyVO = replyService.getSimpleReply(vo.getReplyId());
                if (triggerReplyVO == null) {
                    continue;
                }
                detailResp.setReplyContent(triggerReplyVO.getContent());
                // post notice
                PostVO postVO = postService.getSimplePost(vo.getTargetResourceId());
                UserVO userVO = userService.getUserInfo(vo.getFromUserId());

                BeanUtils.copyProperties(detailResp, vo);
                if (postVO.getAnonymous() == Constants.POST_ANONYMOUS.ANONYMOUS) {
                    detailResp.setFromUserAvatarImgUrl(userVO.getAnonXiaUrl());
                    detailResp.setFromUserNickName(userVO.getAnonNickName());
                } else {
                    detailResp.setFromUserAvatarImgUrl(userVO.getAvatarUrl());
                    detailResp.setFromUserNickName(userVO.getNickName());
                }

                NoticePostContentResp postContent = new NoticePostContentResp();
                postContent.setPostId(postVO.getPostId());
                postContent.setPostTitle(postVO.getTitle());
                UserVO postUserVO = userService.getUserInfo(postVO.getUserId());

                if (postVO.getAnonymous() == Constants.POST_ANONYMOUS.ANONYMOUS) {
                    postContent.setPostUserAvatarImgUrl(postUserVO.getAnonLouUrl());
                    postContent.setPostUserNickName(postUserVO.getAnonNickName());
                } else {
                    postContent.setPostUserAvatarImgUrl(postUserVO.getAvatarUrl());
                    postContent.setPostUserNickName(postUserVO.getNickName());
                }
                postContent.setPostUserId(postVO.getUserId());
                detailResp.setContent(postContent);
            } else if (vo.getType() == Constants.NOTICE.TYPE.POST_COLLECTION_NEW_INFO) {
                int postId = vo.getTargetResourceId();
                detailResp.setType(vo.getType());
                PostVO postVO = postService.getSimplePost(postId);
                detailResp.setStatus(vo.getStatus());

                PostDetailResp postDetailResp = new PostDetailResp();

                BeanUtils.copyProperties(postDetailResp, postVO);
                userService.setUserInfoForPost(postDetailResp, postVO.getUserId());

                for (ImgVO imgVO : postVO.getImgVOList()) {
                    ImgDetailResp imgResp = new ImgDetailResp();
                    imgResp.setImgId(imgVO.getId());
                    imgResp.setUrl(imgVO.getUrl());
                    postDetailResp.getImgList().add(imgResp);
                }
                detailResp.setContent(postDetailResp);

            } else {
                // reply notice
                // 拷贝notice的基本信息
                BeanUtils.copyProperties(detailResp, vo);
                // 获取notice的fromUser信息
                ReplyVO triggerReplyVO = replyService.getSimpleReply(vo.getReplyId());
                if (triggerReplyVO == null) {
                    continue;
                }
                detailResp.setReplyContent(triggerReplyVO.getContent());
                UserVO fromUserVO = userService.getUserInfo(vo.getFromUserId());
                ReplyVO targetReplyVO = replyService.getSimpleReply(vo.getTargetResourceId());
                if (triggerReplyVO == null) {
                    continue;
                }
                PostVO postVO = postService.getSimplePost(targetReplyVO.getPostId());

                if (triggerReplyVO.getAnonymous() == Constants.REPLY_ANONYMOUS.ANONYMOUS ||
                        postVO.getAnonymous() == Constants.REPLY_ANONYMOUS.ANONYMOUS) {
                    detailResp.setFromUserAvatarImgUrl(fromUserVO.getAnonXiaUrl());
                    detailResp.setFromUserNickName(fromUserVO.getAnonNickName());
                } else {
                    detailResp.setFromUserAvatarImgUrl(fromUserVO.getAvatarUrl());
                    detailResp.setFromUserNickName(fromUserVO.getNickName());
                }

                // 组建ReplyContent
                NoticeReplyContentResp replyContent = new NoticeReplyContentResp();
                // 获取replyContent中post相关信息
                UserVO postUserVO = userService.getUserInfo(postVO.getUserId());
                if (postVO.getAnonymous() == Constants.POST_ANONYMOUS.ANONYMOUS) {
                    replyContent.setPostUserAvatarImgUrl(postUserVO.getAnonLouUrl());
                    replyContent.setPostUserNickName(postUserVO.getAnonNickName());
                } else {
                    replyContent.setPostUserAvatarImgUrl(postUserVO.getAvatarUrl());
                    replyContent.setPostUserNickName(postUserVO.getNickName());
                }
                // 获取replyContent中post的其他字段
                replyContent.setPostId(postVO.getPostId());
                replyContent.setPostTitle(postVO.getTitle());
                replyContent.setPostUserId(postVO.getUserId());


                // 获取replyContent中reply的相关信息
                replyContent.setTargetReplyId(targetReplyVO.getReplyId());
                replyContent.setTargetReplyUserId(targetReplyVO.getUserId());
                UserVO replyUserVO = userService.getUserInfo(targetReplyVO.getUserId());

                if (postVO.getAnonymous() == Constants.POST_ANONYMOUS.ANONYMOUS
                        || targetReplyVO.getAnonymous() == Constants.REPLY_ANONYMOUS.ANONYMOUS) {
                    replyContent.setTargetReplyUserAvatarImgUrl(replyUserVO.getAnonXiaUrl());
                    replyContent.setTargetReplyUserNickName(replyUserVO.getAnonNickName());
                } else {
                    replyContent.setTargetReplyUserAvatarImgUrl(replyUserVO.getAvatarUrl());
                    replyContent.setTargetReplyUserNickName(replyUserVO.getNickName());
                }

                replyContent.setTargetReplyContent(targetReplyVO.getContent());
                if (targetReplyVO.getLastReplyId() != 0) {
                    ReplyVO level2ReplyVO = replyService.getSimpleReply(targetReplyVO.getLastReplyId());
                    if (level2ReplyVO == null) {
                        continue;
                    }
                    UserVO level2ReplyUserVO = userService.getUserInfo(level2ReplyVO.getUserId());
                    replyContent.setTargetReplyRepliedUserId(level2ReplyVO.getUserId());
                    if (postVO.getAnonymous() != Constants.POST_ANONYMOUS.ANONYMOUS
                            && level2ReplyVO.getAnonymous() != Constants.POST_ANONYMOUS.ANONYMOUS) {
                        replyContent.setTargetReplyRepliedUserAvatarImgUrl(level2ReplyUserVO.getAvatarUrl());
                        replyContent.setTargetReplyRepliedUserNickName(level2ReplyUserVO.getNickName());
                    } else {
                        replyContent.setTargetReplyRepliedUserAvatarImgUrl(level2ReplyUserVO.getAnonXiaUrl());
                        replyContent.setTargetReplyRepliedUserNickName(level2ReplyUserVO.getAnonNickName());
                    }

                }
                detailResp.setContent(replyContent);
            }
            resp.getList().add(detailResp);
        }
        return resp;
    }


    @Action(name = "notice.readAll")
    @NeedToken
    public CommonResultResp readAll(CommandContext ctxt, NoticeReadAllReq req) {
        noticeService.readAll(ctxt.getUid());
        return CommonResultResp.success();
    }
}
