package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.ryze.api.req.AdminReplyAddReq;
import com.j13.ryze.api.req.AdminReplyDeleteReq;
import com.j13.ryze.api.req.AdminReplyListReq;
import com.j13.ryze.api.req.AdminReplyUpdateContentReq;
import com.j13.ryze.api.resp.AdminReplyAddResp;
import com.j13.ryze.api.resp.AdminReplyListResp;
import org.springframework.stereotype.Component;

@Component
public class AdminReplyFacade {


    @Action(name = "admin.reply.add", desc = "")
    public AdminReplyAddResp postAdd(CommandContext ctxt, AdminReplyAddReq req) {
        return null;
    }


    @Action(name = "admin.reply.list")
    public AdminReplyListResp postList(CommandContext ctxt, AdminReplyListReq req) {
        return null;
    }

    @Action(name="admin.reply.updateContent")
    public CommonResultResp postUpdateContent(CommandContext ctxt, AdminReplyUpdateContentReq req){

        return CommonResultResp.success();
    }

    @Action(name="admin.reply.delete")
    public CommonResultResp postDelete(CommandContext ctxt, AdminReplyDeleteReq req){
        return CommonResultResp.success();
    }


}
