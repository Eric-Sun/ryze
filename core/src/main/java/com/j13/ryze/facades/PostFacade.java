package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.ryze.api.req.PostAddReq;
import com.j13.ryze.api.req.PostDeleteReq;
import com.j13.ryze.api.req.PostListReq;
import com.j13.ryze.api.req.PostUpdateContentReq;
import com.j13.ryze.api.resp.PostAddResp;
import com.j13.ryze.api.resp.PostListResp;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {


    @Action(name = "post.add", desc = "")
    public PostAddResp postAdd(CommandContext ctxt, PostAddReq req) {
        return null;
    }


    @Action(name = "post.list")
    public PostListResp postList(CommandContext ctxt, PostListReq req) {
        return null;
    }

    @Action(name="post.updateContent")
    public CommonResultResp postUpdateContent(CommandContext ctxt, PostUpdateContentReq req){

        return CommonResultResp.success();
    }

    @Action(name="post.delete")
    public CommonResultResp postDelete(CommandContext ctxt, PostDeleteReq req){
        return CommonResultResp.success();
    }


}
