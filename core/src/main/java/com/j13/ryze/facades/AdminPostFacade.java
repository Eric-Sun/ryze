package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.*;
import com.j13.ryze.api.resp.*;
import com.j13.ryze.cache.PostIdListCache;
import com.j13.ryze.core.Constants;
import com.j13.ryze.daos.*;
import com.j13.ryze.services.AdminLevelInfoService;
import com.j13.ryze.services.PostService;
import com.j13.ryze.services.ReplyService;
import com.j13.ryze.services.UserService;
import com.j13.ryze.vos.ImgVO;
import com.j13.ryze.vos.PostVO;
import com.j13.ryze.vos.TopicVO;
import com.j13.ryze.vos.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminPostFacade {

    @Autowired
    PostDAO postDAO;

    @Autowired
    BarDAO barDAO;

    @Autowired
    BarMemberDAO barMemberDAO;
    @Autowired
    StarPostDAO starPostDAO;
    @Autowired
    UserService userService;
    @Autowired
    AdminLevelInfoService adminLevelInfoService;
    @Autowired
    PostService postService;
    @Autowired
    ReplyService replyService;
    @Autowired
    FPostDAO fPostDAO;
    @Autowired
    FReplyDAO fReplyDAO;
    @Autowired
    PostIdListCache postIdListCache;

    @Action(name = "admin.post.add", desc = "")
    public AdminPostAddResp add(CommandContext ctxt, AdminPostAddReq req) {
        AdminPostAddResp resp = new AdminPostAddResp();
//        if (!barDAO.exist(req.getBarId())) {
//            throw new CommonException(ErrorCode.Bar.NOT_EXIST);
//        }
//
//        if (!barMemberDAO.hasMember(req.getBarId(), req.getUserId())) {
//            throw new CommonException(ErrorCode.Bar.NOT_HAS_MEMBER);
//        }

        int postId = postService.add(req.getUserId(),
                req.getBarId(), req.getTitle(), req.getContent(), req.getAnonymous(), req.getType(), req.getImgList(), req.getTopicIdList());
        resp.setPostId(postId);
        return resp;
    }

    @Action(name = "admin.post.list", desc = "")
    public AdminPostListResp list(CommandContext ctxt, AdminPostListReq req) {
        AdminPostListResp resp = new AdminPostListResp();

        String barName = barDAO.getBarName(req.getBarId());
        resp.setBarName(barName);
        List<PostVO> postVOList = postService.listForAdmin(req.getBarId(), Constants.POST_TYPE.ALL_TYPE, req.getPageNum(), req.getSize());
        int count = postService.postCount(req.getBarId());
        resp.setCount(count);

        for (PostVO postVO : postVOList) {
            AdminPostDetailResp detailResp = new AdminPostDetailResp();
            BeanUtils.copyProperties(detailResp, postVO);
            boolean b = starPostDAO.checkStar(postVO.getPostId());
            if (b) {
                detailResp.setStar(1);
            }

            int replyCount = replyService.getReplyCount(postVO.getPostId());

            for (ImgVO imgVO : postVO.getImgVOList()) {
                ImgDetailResp imgResp = new ImgDetailResp();
                imgResp.setImgId(imgVO.getId());
                imgResp.setUrl(imgVO.getUrl());
                detailResp.getImgList().add(imgResp);
            }

            for (TopicVO topicVO : postVO.getTopicList()) {
                AdminTopicDetailResp topicResp = new AdminTopicDetailResp();
                topicResp.setTopicId(topicVO.getId());
                topicResp.setTopicName(topicVO.getName());
                detailResp.getTopicList().add(topicResp);
            }

            resp.getList().add(detailResp);
        }
        return resp;
    }

    @Action(name = "admin.post.update")
    public CommonResultResp updateContentAndTitle(CommandContext ctxt, AdminPostUpdateContentReq req) {
        postService.update(req.getPostId(), req.getContent(), req.getTitle(), req.getAnonymous(), req.getType(), req.getImgListStr());
        return CommonResultResp.success();
    }


    @Action(name = "admin.post.delete")
    public CommonResultResp delete(CommandContext ctxt, AdminPostDeleteReq req) {
        postDAO.delete(req.getPostId());
        postIdListCache.removePostId(req.getPostId());
        barDAO.reducePostCount(req.getBarId());
        return CommonResultResp.success();
    }

    @Action(name = "admin.post.detail")
    public AdminPostDetailResp detail(CommandContext ctxt, AdminPostDetailReq req) {
        AdminPostDetailResp resp = new AdminPostDetailResp();
        PostVO vo = postService.getSimplePost(req.getPostId());
        BeanUtils.copyProperties(resp, vo);
        UserVO user = userService.getUserInfo(vo.getUserId());
        resp.setUserName(user.getNickName());
        resp.setUserAvatarUrl(user.getAvatarUrl());
        for (ImgVO imgVO : vo.getImgVOList()) {
            ImgDetailResp imgResp = new ImgDetailResp();
            imgResp.setImgId(imgVO.getId());
            imgResp.setUrl(imgVO.getUrl());
            resp.getImgList().add(imgResp);
        }

        for (TopicVO topicVO : vo.getTopicList()) {
            AdminTopicDetailResp topicResp = new AdminTopicDetailResp();
            topicResp.setTopicId(topicVO.getId());
            topicResp.setTopicName(topicVO.getName());
            resp.getTopicList().add(topicResp);
        }


        List<AdminLevelInfoResp> levelInfoList = adminLevelInfoService.findLevelInfo(vo.getPostId(), 1);
        resp.setLevelInfo(levelInfoList);

        return resp;
    }

    @Action(name="admin.post.updateTopicList")
    public CommonResultResp updateTopicList(CommandContext ctxt, AdminPostUpdateTopicListReq req){
        postService.updateTopicList(req.getPostId(),req.getTopicIdList());
    }


    @Action(name = "admin.post.updateImg")
    public CommonResultResp updateImg(CommandContext ctxt, AdminPostUpdateImgReq req) {
        postDAO.updateImg(req.getPostId(), req.getImgIdListStr());
        postService.flushSimplePost(req.getPostId());
        return CommonResultResp.success();
    }

    @Action(name = "admin.post.offline")
    public CommonResultResp offline(CommandContext ctxt, AdminPostOfflineReq req) {
        postDAO.offline(req.getPostId());
        postIdListCache.removePostId(req.getPostId());
        return CommonResultResp.success();
    }

    @Action(name = "admin.post.online")
    public CommonResultResp online(CommandContext ctxt, AdminPostOnlineReq req) {
        postDAO.online(req.getPostId());
        postIdListCache.addPostId(req.getPostId());
        return CommonResultResp.success();
    }

    @Action(name = "admin.post.offlineList")
    public AdminPostOfflineListResp offlineList(CommandContext ctxt, AdminPostOfflineListReq req) {
        AdminPostOfflineListResp resp = new AdminPostOfflineListResp();

        List<PostVO> postVOList = postService.offlineList(req.getBarId(), req.getPageNum(), req.getSize());
        int count = postService.offlineListCount(req.getBarId());

        for (PostVO postVO : postVOList) {

            // 获取对应postId的总抓取评论的量
            int fReplyCount = fPostDAO.getFReplyCount(postVO.getPostId());

            AdminPostDetailResp detailResp = new AdminPostDetailResp();
            detailResp.setfReplyCount(fReplyCount);
            BeanUtils.copyProperties(detailResp, postVO);
            boolean b = starPostDAO.checkStar(postVO.getPostId());
            if (b) {
                detailResp.setStar(1);
            }

            for (ImgVO imgVO : postVO.getImgVOList()) {
                ImgDetailResp imgResp = new ImgDetailResp();
                imgResp.setImgId(imgVO.getId());
                imgResp.setUrl(imgVO.getUrl());
                detailResp.getImgList().add(imgResp);
            }
            resp.getData().add(detailResp);
        }
        resp.setCount(count);
        return resp;
    }

    @Action(name = "admin.post.queryByTitle", desc = "已废弃")
    public AdminPostQueryByTitleResp queryByTitle(CommandContext ctxt, AdminPostQueryByTitleReq req) {
        AdminPostQueryByTitleResp resp = new AdminPostQueryByTitleResp();
        List<PostVO> list = postDAO.queryByTtile(req.getBarId(), req.getName(), req.getPageNum(), req.getSize());
        for (PostVO vo : list) {
            AdminPostDetailResp r = new AdminPostDetailResp();
            BeanUtils.copyProperties(r, vo);
            UserVO user = userService.getUserInfo(vo.getUserId());
            r.setUserName(user.getNickName());
            r.setUserAvatarUrl(user.getAvatarUrl());
            resp.getList().add(r);
        }
        return resp;
    }

    @Action(name = "admin.post.query")
    public AdminPostQueryResp query(CommandContext ctxt, AdminPostQueryReq req) {
        AdminPostQueryResp resp = new AdminPostQueryResp();
        List<Integer> postIdList = postDAO.query(req.getBarId(), req.getPostId(), req.getTitle(), req.getUserId(), req.getPageNum(), req.getSize());
        for (Integer postId : postIdList) {
            AdminPostDetailResp r = new AdminPostDetailResp();
            PostVO vo = postService.getSimplePost(postId);
            BeanUtils.copyProperties(r, vo);
            UserVO user = userService.getUserInfo(vo.getUserId());
            r.setUserName(user.getNickName());
            r.setUserAvatarUrl(user.getAvatarUrl());
            resp.getList().add(r);
        }
        return resp;
    }

    @Action(name = "admin.post.flushFPostReplyCount")
    public CommonResultResp flushFPostReplyCount(CommandContext ctxt, AdminPostFlushFPostReplyCountReq req) {

        List<Integer> fPostIdList = fPostDAO.getSourcePostIdList();
        for (Integer fPostId : fPostIdList) {
            int replyCount = fReplyDAO.countReplyCount(fPostId);
            fPostDAO.updateReplyCountBySourcePostId(fPostId, replyCount);
        }
        return CommonResultResp.success();
    }

    @Action(name = "admin.post.queryByUserId", desc = "已废弃")
    public AdminPostQueryByUserIdResp queryByUserId(CommandContext ctxt, AdminPostQueryByUserIdReq req) {
        AdminPostQueryByUserIdResp resp = new AdminPostQueryByUserIdResp();
        List<PostVO> list = postDAO.queryByUserId(req.getBarId(), req.getUserId(), req.getPageNum(), req.getSize());
        for (PostVO vo : list) {
            AdminPostDetailResp r = new AdminPostDetailResp();
            BeanUtils.copyProperties(r, vo);
            UserVO user = userService.getUserInfo(vo.getUserId());
            r.setUserName(user.getNickName());
            r.setUserAvatarUrl(user.getAvatarUrl());
            resp.getList().add(r);
        }
        return resp;
    }

    @Action(name = "admin.post.queryByPostId", desc = "已废弃")
    public AdminPostQueryByPostIdResp queryByPostId(CommandContext ctxt, AdminPostQueryByPostIdReq req) {
        AdminPostQueryByPostIdResp resp = new AdminPostQueryByPostIdResp();
        AdminPostDetailResp r = new AdminPostDetailResp();
        PostVO postVO = postService.getSimplePost(req.getPostId());
        BeanUtils.copyProperties(r, postVO);
        UserVO user = userService.getUserInfo(postVO.getUserId());
        r.setUserName(user.getNickName());
        r.setUserAvatarUrl(user.getAvatarUrl());
        resp.getList().add(r);
        return resp;
    }

    @Action(name = "admin.post.deletedList")
    public AdminPostDeletedListResp deletedList(CommandContext ctxt, AdminPostDeletedListReq req) {

        AdminPostDeletedListResp resp = new AdminPostDeletedListResp();

        List<PostVO> postVOList = postService.deletedList(req.getBarId(), req.getPageNum(), req.getSize());
        int count = postService.deletedListCount(req.getBarId());

        for (PostVO postVO : postVOList) {
            AdminPostDetailResp detailResp = new AdminPostDetailResp();
            BeanUtils.copyProperties(detailResp, postVO);
            boolean b = starPostDAO.checkStar(postVO.getPostId());
            if (b) {
                detailResp.setStar(1);
            }

            for (ImgVO imgVO : postVO.getImgVOList()) {
                ImgDetailResp imgResp = new ImgDetailResp();
                imgResp.setImgId(imgVO.getId());
                imgResp.setUrl(imgVO.getUrl());
                detailResp.getImgList().add(imgResp);
            }
            resp.getList().add(detailResp);
        }
        resp.setCount(count);
        return resp;
    }

    @Action(name = "admin.post.undoDelete")
    public CommonResultResp undoDelete(CommandContext ctxt, AdminPostUndoDeleteReq req) {
        postDAO.undoDelete(req.getBarId(), req.getPostId());
        // 默认回复删除之后是下架的状态
        postService.offline(req.getPostId());
        return CommonResultResp.success();
    }
}
