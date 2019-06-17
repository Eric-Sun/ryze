package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.anno.NeedToken;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.exceptions.CommonException;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.*;
import com.j13.ryze.api.resp.*;
import com.j13.ryze.core.ErrorCode;
import com.j13.ryze.core.Logger;
import com.j13.ryze.daos.*;
import com.j13.ryze.services.*;
import com.j13.ryze.vos.ImgVO;
import com.j13.ryze.vos.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostFacade {

    @Autowired
    PostDAO postDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    UserService userService;
    @Autowired
    ReplyDAO replyDAO;
    @Autowired
    BarDAO barDAO;
    @Autowired
    BarMemberDAO barMemberDAO;
    @Autowired
    PostService postService;
    @Autowired
    AdminLevelInfoService adminLevelInfoService;
    @Autowired
    NoticeService noticeService;
    @Autowired
    IAcsClientService iAcsClientService;

    @Action(name = "post.list", desc = "type=0:故事贴，1：一日一记，-1：全部")
    @NeedToken
    public PostListResp list(CommandContext ctxt, PostListReq req) {
        // 因为这个是首页，尝试判断用户是否是被锁用户，如果是的话尝试解锁
        int requestUserId = ctxt.getUid();
        userService.tryToUnlockForTimeout(requestUserId);

        PostListResp resp = new PostListResp();
        List<PostVO> list = null;
        list = postService.list(req.getBarId(), req.getType(), req.getPageNum(), req.getSize());
        for (PostVO vo : list) {
            PostDetailResp r = new PostDetailResp();
            BeanUtils.copyProperties(r, vo);
            userService.setUserInfoForPost(r, vo.getUserId());
            r.setReplyCount(postService.replyCount(vo.getPostId()));
            resp.getList().add(r);

            for (ImgVO imgVO : vo.getImgVOList()) {
                ImgDetailResp imgResp = new ImgDetailResp();
                imgResp.setImgId(imgVO.getId());
                imgResp.setUrl(imgVO.getUrl());
                r.getImgList().add(imgResp);
            }

        }

        // 查看notice数量，给tabbar红点
        if (ctxt.getUid() != 0) {
            int noticeSize = noticeService.listNotReadSize(ctxt.getUid());
            resp.setNoticeSize(noticeSize);
        }

        return resp;
    }

    @Action(name = "post.detail", desc = "post detail and replies")
    public PostDetailResp detail(CommandContext ctxt, PostDetailReq req) {
        PostDetailResp resp = new PostDetailResp();
        PostVO vo = postService.getSimplePost(req.getPostId());
        BeanUtils.copyProperties(resp, vo);
        userService.setUserInfoForPost(resp, vo.getUserId());

        for (ImgVO imgVO : vo.getImgVOList()) {
            ImgDetailResp imgResp = new ImgDetailResp();
            imgResp.setImgId(imgVO.getId());
            imgResp.setUrl(imgVO.getUrl());
            resp.getImgList().add(imgResp);
        }

        return resp;
    }

    @Action(name = "post.delete")
    @NeedToken
    public CommonResultResp delete(CommandContext ctxt, PostDeleteReq req) {
        int userId = ctxt.getUid();
        postDAO.delete(req.getPostId(), userId);
        com.j13.ryze.core.Logger.COMMON.info("delete post. postId={}, userId={}", req.getPostId(), userId);
        return CommonResultResp.success();
    }

    @Action(name = "post.recentlyPostList", desc = "用户最近发布的Post")
    @NeedToken
    public PostListResp recentlyPostList(CommandContext ctxt, PostRecentlyPostListReq req) {
        PostListResp resp = new PostListResp();
        List<PostVO> postList = postDAO.listByUserId(req.getBarId(), ctxt.getUid(), req.getPageNum(), req.getSize());
        for (PostVO vo : postList) {
            PostDetailResp r = new PostDetailResp();
            BeanUtils.copyProperties(r, vo);
            userService.setUserInfoForPost(r, vo.getUserId());
            resp.getList().add(r);
        }
        return resp;
    }


    @Action(name = "post.recentlyOtherUserPostList", desc = "用户最近发布的Post")
    @NeedToken
    public PostListResp recentlyOtherUserPostList(CommandContext ctxt, PostRecentlyOtherUserPostListReq req) {
        PostListResp resp = new PostListResp();
        List<PostVO> postList = postDAO.recentlyOtherUserPostList(req.getOtherUserId(), req.getBarId()
                , req.getPageNum(), req.getSize());
        for (PostVO vo : postList) {
            PostDetailResp r = new PostDetailResp();
            BeanUtils.copyProperties(r, vo);
            userService.setUserInfoForPost(r, vo.getUserId());
            resp.getList().add(r);
        }
        return resp;
    }


    @Action(name = "post.recentlyReplyList", desc = "用户最近回复的Post，包含二三级回复")
    @NeedToken
    public PostListResp recentlyReplyList(CommandContext ctxt, PostRecentlyReplyListReq req) {
        PostListResp resp = new PostListResp();
        List<Integer> postIdList = replyDAO.recentlyList(req.getBarId(),
                ctxt.getUid(), req.getPageNum(), req.getSize());

        for (Integer postId : postIdList) {
            PostVO vo = postDAO.get(postId);
            PostDetailResp r = new PostDetailResp();
            BeanUtils.copyProperties(r, vo);
            userService.setUserInfoForPost(r, vo.getUserId());
            resp.getList().add(r);
        }
        return resp;
    }


    @Action(name = "post.add")
    @NeedToken
    public PostAddResp add(CommandContext ctxt, PostAddReq req) {

        boolean b = iAcsClientService.scan(req.getContent());
        if (b == false) {
            throw new CommonException(ErrorCode.Common.CONTENT_ILLEGAL);
        }

        PostAddResp resp = new PostAddResp();
//        if (!barDAO.exist(req.getBarId())) {
//            throw new CommonException(ErrorCode.Bar.NOT_EXIST);
//        }

//        if (!barMemberDAO.hasMember(req.getBarId(), req.getUserId())) {
//            throw new CommonException(ErrorCode.Bar.NOT_HAS_MEMBER);
//        }

        if (req.getImgList() == null || req.getImgList().equals("")) {
            req.setImgList("[]");
        }

        int postId = postService.add(ctxt.getUid(),
                req.getBarId(), req.getTitle(), req.getContent(), req.getAnonymous(), req.getType(), req.getImgList());
        resp.setPostId(postId);
        return resp;
    }


    @Action(name = "post.collect.add", desc = "收藏帖子")
    @NeedToken
    public PostCollectAddResp collectAdd(CommandContext ctxt, PostCollectAddReq req) {
        PostCollectAddResp resp = new PostCollectAddResp();
        int userId = ctxt.getUid();
        int postId = req.getPostId();

        int collectId = postService.collectAdd(userId, postId);
        Logger.COMMON.info("collect post. userId={},postId={},collectId={}", userId, postId, collectId);
        resp.setCollectId(collectId);
        return resp;
    }


    @Action(name = "post.collect.delete")
    public CommonResultResp collectDelete(CommandContext ctxt, PostCollectDeleteReq req) {
        int collectId = req.getCollectId();
        int userId = ctxt.getUid();

        postService.collectDelete(userId, collectId);
        Logger.COMMON.info("delete collected post. userId={},collectId={}", userId, collectId);
        return CommonResultResp.success();
    }

}
