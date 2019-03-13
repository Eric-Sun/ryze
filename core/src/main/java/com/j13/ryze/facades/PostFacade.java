package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.exceptions.CommonException;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.PostDetailReq;
import com.j13.ryze.api.req.PostDetailResp;
import com.j13.ryze.api.req.PostListResp;
import com.j13.ryze.api.resp.AdminPostDetailResp;
import com.j13.ryze.api.resp.AdminReplyDetailResp;
import com.j13.ryze.api.resp.PostListReq;
import com.j13.ryze.core.Constants;
import com.j13.ryze.core.ErrorCode;
import com.j13.ryze.daos.PostDAO;
import com.j13.ryze.daos.ReplyDAO;
import com.j13.ryze.daos.UserDAO;
import com.j13.ryze.services.UserService;
import com.j13.ryze.vos.PostVO;
import com.j13.ryze.vos.ReplyVO;
import com.j13.ryze.vos.UserVO;
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

    @Action(name = "post.list", desc = "type=0:故事贴，1：一日一记，-1：全部")
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
            UserVO user = userService.getUserInfo(vo.getUserId());
            r.setUserName(user.getNickName());
            r.setUserAvatarUrl(user.getAvatarUrl());
            resp.getList().add(r);
        }
        return resp;
    }

    @Action(name = "post.detail", desc = "post detail and replies")
    public PostDetailResp detail(CommandContext ctxt, PostDetailReq req) {
        PostDetailResp resp = new PostDetailResp();
        PostVO vo = postDAO.get(req.getPostId());
        BeanUtils.copyProperties(resp, vo);
        UserVO user = userService.getUserInfo(vo.getUserId());
        resp.setUserName(user.getNickName());
        resp.setUserAvatarUrl(user.getAvatarUrl());
        return resp;
    }




}
