package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.exceptions.CommonException;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.*;
import com.j13.ryze.api.resp.*;
import com.j13.ryze.core.ErrorCode;
import com.j13.ryze.daos.BarDAO;
import com.j13.ryze.daos.BarMemberDAO;
import com.j13.ryze.services.UserService;
import com.j13.ryze.vos.BarVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminBarFacade {
    @Autowired
    BarDAO barDAO;
    @Autowired
    BarMemberDAO barMemberDAO;
    @Autowired
    UserService userService;

    @Action(name = "admin.bar.add")
    public AdminBarAddResp add(CommandContext ctxt, AdminBarAddReq req) {
        AdminBarAddResp resp = new AdminBarAddResp();
        int barId = barDAO.add(req.getUserId(), req.getName());
        int barMemberId = barMemberDAO.addMember(barId, req.getUserId());
        resp.setBarId(barId);
        return resp;
    }

    @Action(name = "admin.bar.addMember")
    public AdminBarMemberAddResp addMember(CommandContext ctxt, AdminBarMemberAddReq req) {
        AdminBarMemberAddResp resp = new AdminBarMemberAddResp();
//        if (barMemberDAO.hasMember(req.getBarId(), req.getUserId())) {
//            throw new CommonException(ErrorCode.Bar.HAS_MEMBER);
//        }

        int barMemberId = barMemberDAO.addMember(req.getBarId(), req.getUserId());
        resp.setBarMemberId(barMemberId);
        return resp;

    }

    @Action(name = "admin.bar.deleteMember")
    public CommonResultResp deleteMember(CommandContext ctxt, AdminBarMemberDeleteReq req) {
//        if (!barMemberDAO.hasMember(req.getBarId(), req.getUserId())) {
//            throw new CommonException(ErrorCode.Bar.NOT_HAS_MEMBER);
//        }

        barMemberDAO.deleteMember(req.getUserId(), req.getBarId());
        return CommonResultResp.success();
    }

    @Action(name = "admin.bar.delete")
    public CommonResultResp delete(CommandContext ctxt, AdminBarDeleteReq req) {
//        if (!barDAO.checkBarOwner(req.getBarId(), req.getUserId())) {
//            throw new CommonException(ErrorCode.Bar.NOT_BAR_OWNER);
//        }
        barDAO.delete(req.getBarId());
        return CommonResultResp.success();
    }


    @Action(name = "admin.bar.list")
    public AdminBarListResp list(CommandContext ctxt, AdminBarListReq req) {
        AdminBarListResp resp = new AdminBarListResp();
        List<BarVO> list = barDAO.list(req.getSize(), req.getPageNum());
        for (BarVO vo : list) {
            AdminBarDetailResp r = new AdminBarDetailResp();
            BeanUtils.copyProperties(r, vo);
            r.setUserName(userService.getNickName(vo.getUserId()));
            resp.getData().add(r);
        }
        return resp;
    }

    @Action(name = "admin.bar.query")
    public AdminBarQueryResp query(CommandContext ctxt, AdminBarQueryReq req) {
        AdminBarQueryResp resp = new AdminBarQueryResp();
        List<BarVO> list = barDAO.queryForBarName(req.getQueryBarName(), req.getSize(), req.getPageNum());
        for (BarVO vo : list) {
            AdminBarDetailResp r = new AdminBarDetailResp();
            BeanUtils.copyProperties(r, vo);
            r.setUserName(userService.getNickName(vo.getUserId()));
            resp.getData().add(r);
        }
        return resp;
    }

}
