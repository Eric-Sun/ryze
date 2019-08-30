package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.ryze.api.req.MemberUnifiedOrderReq;
import com.j13.ryze.api.resp.MemberUnifiedOrderResp;
import org.springframework.stereotype.Component;

@Component
public class MemberFacade {


    @Action(name = "member.unifiedOrder",desc="创建统一订单")
    public MemberUnifiedOrderResp unifiedOrder(CommandContext ctxt, MemberUnifiedOrderReq req) {
        MemberUnifiedOrderResp resp = new MemberUnifiedOrderResp();


        return resp;
    }


}
