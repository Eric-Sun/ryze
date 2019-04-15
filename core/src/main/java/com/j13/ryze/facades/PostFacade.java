package com.j13.ryze.facades;

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
import com.j13.ryze.daos.*;
import com.j13.ryze.services.AdminLevelInfoService;
import com.j13.ryze.services.NoticeService;
import com.j13.ryze.services.PostService;
import com.j13.ryze.services.UserService;
import com.j13.ryze.vos.PostVO;
import com.j13.ryze.vos.ReplyVO;
import com.j13.ryze.vos.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostFacade {
    private static Logger LOG = LoggerFactory.getLogger(PostFacade.class);

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

    @Action(name = "post.list", desc = "type=0:故事贴，1：一日一记，-1：全部")
    @NeedToken
    public PostListResp list(CommandContext ctxt, PostListReq req) {
        PostListResp resp = new PostListResp();
        List<PostVO> list = null;
        if (req.getType() == Constants.POST_TYPE.ALL_TYPE) {
            list = postDAO.list(req.getBarId(), req.getPageNum(), req.getSize());
        } else if (req.getType() == Constants.POST_TYPE.STORE) {
            list = postDAO.listByType(req.getBarId(), req.getType(), req.getPageNum(), req.getSize());
        } else if (req.getType() == Constants.POST_TYPE.DIARY) {
            list = postDAO.listByType(req.getBarId(), req.getType(), req.getPageNum(), req.getSize());
        } else {
            throw new CommonException(ErrorCode.POST.QUERY_POST_TYPE_ERROR, "type=" + req.getType());
        }
        for (PostVO vo : list) {
            PostDetailResp r = new PostDetailResp();
            BeanUtils.copyProperties(r, vo);
            userService.setUserInfoForPost(r, vo.getUserId());
            r.setReplyCount(postService.replyCount(vo.getPostId()));
            resp.getList().add(r);
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
        PostVO vo = postDAO.get(req.getPostId());
        BeanUtils.copyProperties(resp, vo);
        userService.setUserInfoForPost(resp, vo.getUserId());
        return resp;
    }

    @Action(name = "post.delete")
    @NeedToken
    public CommonResultResp delete(CommandContext ctxt, PostDeleteReq req) {
        int userId = ctxt.getUid();
        postDAO.delete(req.getPostId(), userId);
        LOG.info("delete post. postId={}, userId={}", req.getPostId(), userId);
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
        PostAddResp resp = new PostAddResp();
//        if (!barDAO.exist(req.getBarId())) {
//            throw new CommonException(ErrorCode.Bar.NOT_EXIST);
//        }

//        if (!barMemberDAO.hasMember(req.getBarId(), req.getUserId())) {
//            throw new CommonException(ErrorCode.Bar.NOT_HAS_MEMBER);
//        }

        int id = postDAO.add(ctxt.getUid(),
                req.getBarId(), req.getTitle(), req.getContent(), req.getAnonymous(), req.getType());
        barDAO.addPostCount(req.getBarId());
        resp.setPostId(id);
        return resp;
    }


}
