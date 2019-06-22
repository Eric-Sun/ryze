package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.anno.NeedToken;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.ryze.api.req.CollectionPostAddReq;
import com.j13.ryze.api.req.CollectionPostDeleteReq;
import com.j13.ryze.api.req.CollectionPostListReq;
import com.j13.ryze.api.req.PostDetailResp;
import com.j13.ryze.api.resp.CollectionPostDetailResp;
import com.j13.ryze.api.resp.CollectionPostListResp;
import com.j13.ryze.api.resp.PostCollectionAddResp;
import com.j13.ryze.core.Constants;
import com.j13.ryze.core.Logger;
import com.j13.ryze.services.CollectionService;
import com.j13.ryze.services.PostService;
import com.j13.ryze.services.UserService;
import com.j13.ryze.vos.CollectionVO;
import com.j13.ryze.vos.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CollectionFacade {

    @Autowired
    CollectionService collectionService;

    @Autowired
    UserService userService;
    @Autowired
    PostService postService;

    @Action(name = "collection.post.add", desc = "收藏帖子")
    @NeedToken
    public PostCollectionAddResp collectionAdd(CommandContext ctxt, CollectionPostAddReq req) {
        PostCollectionAddResp resp = new PostCollectionAddResp();
        int userId = ctxt.getUid();
        int postId = req.getPostId();

        int collectId = collectionService.collectionAdd(userId, postId);
        Logger.COMMON.info("collect post. userId={},postId={},collectId={}", userId, postId, collectId);
        resp.setCollectId(collectId);
        return resp;
    }


    @Action(name = "collection.post.delete", desc = "删除收藏")
    @NeedToken
    public CommonResultResp collectionDelete(CommandContext ctxt, CollectionPostDeleteReq req) {
        int postId = req.getPostId();
        int userId = ctxt.getUid();

        collectionService.collectionDelete(userId, postId);
        Logger.COMMON.info("delete collected post. userId={},postId={}", userId, postId);
        return CommonResultResp.success();
    }

    @Action(name = "collection.post.list", desc = "收藏列表展示（帖子）")
    @NeedToken
    public CollectionPostListResp collectionPostList(CommandContext ctxt, CollectionPostListReq req) {
        CollectionPostListResp resp = new CollectionPostListResp();
        List<CollectionVO> collectionVOList = collectionService.collectionList(ctxt.getUid(), Constants.Collection.Type.POST, req.getPageNum(), req.getSize());
        for (CollectionVO vo : collectionVOList) {
            CollectionPostDetailResp detailResp = new CollectionPostDetailResp();
            detailResp.setCollectionId(vo.getId());

            //
            PostDetailResp postResp = new PostDetailResp();
            BeanUtils.copyProperties(vo.getResourceObject(), postResp);
            UserVO userVO = userService.getUserInfo(postResp.getUserId());
            postResp.setUserAvatarUrl(userVO.getAvatarUrl());
            postResp.setUserName(userVO.getNickName());

            postService.tryToCutOutContent(postResp);


            detailResp.setPost(postResp);
            resp.getList().add(detailResp);
        }
        return resp;
    }

}
