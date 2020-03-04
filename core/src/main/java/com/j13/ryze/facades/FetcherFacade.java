package com.j13.ryze.facades;


import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.ryze.api.req.FetchTianyaOnePostReq;
import com.j13.ryze.fetcher.FetcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class FetcherFacade {

    @Autowired
    FetcherService fetcherService;

    @Action(name="fetch.fetchTianyaOnePost")
    public CommonResultResp fetchTianyaOnePost(CommandContext ctxt, FetchTianyaOnePostReq req){
        fetcherService.triggerFetchTianyaByPostId(req.getPostId());
        return CommonResultResp.success();
    }

}
