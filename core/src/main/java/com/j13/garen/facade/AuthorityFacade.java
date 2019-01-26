package com.j13.garen.facade;

import com.alibaba.fastjson.JSON;
import com.j13.garen.daos.AuthorityDAO;
import com.j13.garen.api.req.*;
import com.j13.garen.api.resp.*;
import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.util.BeanUtils;
import com.j13.garen.vos.AuthorityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorityFacade {


    @Autowired
    AuthorityDAO authorityDAO;

    @Action(name = "authority.list", desc = "authority list.")
    public AuthorityListResp list(CommandContext ctxt, AuthorityListReq req) {
        AuthorityListResp resp = new AuthorityListResp();
        List<AuthorityVO> voList = authorityDAO.list();
        for (AuthorityVO vo : voList) {
            AuthorityGetResp r = new AuthorityGetResp();
            BeanUtils.copyProperties(r, vo);
            resp.getList().add(r);
        }
        return resp;
    }


    @Action(name = "authority.add", desc = "add a new authority")
    public AuthorityAddResp add(CommandContext ctxt, AuthorityAddReq req) {
        AuthorityAddResp resp = new AuthorityAddResp();
        String name = req.getName();
        String[] resourceIds = JSON.parseObject(req.getResouceIdArray(), String[].class);
        int id = authorityDAO.insert(name, resourceIds);
        resp.setId(id);
        return resp;
    }


    @Action(name = "authority.update", desc = " update an authority")
    public CommonResultResp update(CommandContext ctxt, AuthorityUpdateReq req) {
        CommonResultResp resp = new CommonResultResp();
        int id = req.getId();
        String name = req.getName();
        String[] resourceIds = JSON.parseObject(req.getResourceIdArray(), String[].class);
        authorityDAO.update(id, name, resourceIds);
        return resp;
    }

    @Action(name = "authority.delete", desc = " delete an authority")
    public CommonResultResp delete(CommandContext ctxt, AuthorityDeleteReq req) {
        CommonResultResp resp = new CommonResultResp();
        int id = req.getId();
        authorityDAO.delete(id);
        return resp;
    }

    @Action(name = "authority.get", desc = " get an authority")
    public AuthorityGetResp get(CommandContext ctxt, AuthorityGetReq req) {
        AuthorityGetResp resp = new AuthorityGetResp();
        int id = req.getId();
        AuthorityVO vo = authorityDAO.get(id);
        BeanUtils.copyProperties(resp, vo);
        return resp;
    }
}
