package com.j13.garen.facade;

import com.j13.garen.daos.ResourceDAO;
import com.j13.garen.api.req.*;
import com.j13.garen.api.resp.*;
import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.util.BeanUtils;
import com.j13.garen.vos.AuthorityVO;
import com.j13.garen.vos.ResourceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResourceFacade {

    @Autowired
    ResourceDAO resourceDAO;

    @Action(name = "resource.getAuthorityListByResourceName", desc = "get authority list by resource name ")
    public AuthorityListResp getAuthorityListByResourceName(CommandContext ctxt, ResourcetAuthorityListByResourceNameReq req) {
        AuthorityListResp resp = new AuthorityListResp();
        List<AuthorityVO> list = resourceDAO.getAuthorityListByResourceName(req.getName());
        for (AuthorityVO vo : list) {
            AuthorityGetResp r = new AuthorityGetResp();
            BeanUtils.copyProperties(r, vo);
            resp.getList().add(r);
        }
        return resp;
    }

    @Action(name = "resource.add", desc = "resource add")
    public ResourceAddResp add(CommandContext ctxt, ResourceAddReq req) {
        ResourceAddResp resp = new ResourceAddResp();
        String name = req.getName();
        int id = resourceDAO.add(name);
        resp.setResourceId(id);
        return resp;
    }

    @Action(name = "resource.delete", desc = " delete resource ")
    public CommonResultResp delete(CommandContext ctxt, ResourceDeleteReq req) {
        CommonResultResp resp = new CommonResultResp();
        int id = req.getId();
        resourceDAO.delete(id);
        return resp;
    }

    @Action(name = "resource.update", desc = "update resource object by resource's id")
    public CommonResultResp update(CommandContext ctxt, ResourceUpdateReq req) {
        CommonResultResp resp = new CommonResultResp();
        int id = req.getId();
        String name = req.getName();
        resourceDAO.update(id, name);
        return resp;
    }

    @Action(name = "resource.get", desc = " get a resource")
    public ResourceGetResp get(CommandContext ctxt, ResourceGetReq req) {
        ResourceGetResp resp = new ResourceGetResp();
        int id = req.getId();
        ResourceVO resource = resourceDAO.get(id);
        BeanUtils.copyProperties(resp, resource);
        return resp;
    }

    @Action(name = "resource.list", desc = " get a resource list")
    public ResourceListResp list(CommandContext ctxt, ResourceListReq req) {
        ResourceListResp resp = new ResourceListResp();
        List<ResourceVO> list = resourceDAO.getResourceList();
        for (ResourceVO vo : list) {
            ResourceGetResp r = new ResourceGetResp();
            BeanUtils.copyProperties(r, vo);
            resp.getList().add(r);
        }
        return resp;
    }

    @Action(name="resource.getResourceListByAuthorityId", desc="get resource id list by authority id")
    public ResourceGetResourceListByAuthorityIdResp getResourceListByAuthorityId(CommandContext ctxt, ResourceGetResourceListByAuthorityIdReq req) {
        ResourceGetResourceListByAuthorityIdResp resp = new ResourceGetResourceListByAuthorityIdResp();
        int id = req.getAuthorityId();
        List<Integer> list = resourceDAO.getResourceListByAuthorityId(id);
        resp.setList(list);
        return resp;
    }
}
