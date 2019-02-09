package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.exceptions.CommonException;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.*;
import com.j13.ryze.api.resp.BarAddResp;
import com.j13.ryze.api.resp.BarDetailResp;
import com.j13.ryze.api.resp.BarListResp;
import com.j13.ryze.api.resp.BarMemberAddResp;
import com.j13.ryze.core.ErrorCode;
import com.j13.ryze.daos.BarDAO;
import com.j13.ryze.daos.BarMemberDAO;
import com.j13.ryze.services.UserService;
import com.j13.ryze.vos.BarVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BarFacade {
    @Autowired
    BarDAO barDAO;
    @Autowired
    BarMemberDAO barMemberDAO;
    @Autowired
    UserService userService;

    @Action(name = "bar.add")
    public BarAddResp add(CommandContext ctxt, BarAddReq req) {
        BarAddResp resp = new BarAddResp();
        int barId = barDAO.add(req.getUserId(), req.getName());
        int barMemberId = barMemberDAO.addMember(barId, req.getUserId());
        resp.setBarId(barId);
        return resp;
    }

    @Action(name = "bar.addMember")
    public BarMemberAddResp addMember(CommandContext ctxt, BarMemberAddReq req) {
        BarMemberAddResp resp = new BarMemberAddResp();
        if (barMemberDAO.hasMember(req.getBarId(), req.getUserId())) {
            throw new CommonException(ErrorCode.Bar.HAS_MEMBER);
        }

        int barMemberId = barMemberDAO.addMember(req.getBarId(), req.getUserId());
        resp.setBarMemberId(barMemberId);
        return resp;

    }

    @Action(name = "bar.deleteMember")
    public CommonResultResp deleteMember(CommandContext ctxt, BarMemberDeleteReq req) {
        if (!barMemberDAO.hasMember(req.getBarId(), req.getUserId())) {
            throw new CommonException(ErrorCode.Bar.NOT_HAS_MEMBER);
        }

        barMemberDAO.deleteMember(req.getUserId(), req.getBarId());
        return CommonResultResp.success();
    }

    @Action(name = "bar.delete")
    public CommonResultResp delete(CommandContext ctxt, BarDeleteReq req) {
        if (!barDAO.checkBarOwner(req.getBarId(), req.getUserId())) {
            throw new CommonException(ErrorCode.Bar.NOT_BAR_OWNER);
        }
        barDAO.delete(req.getUserId(), req.getBarId());
        return CommonResultResp.success();
    }


    @Action(name = "bar.list")
    public BarListResp list(CommandContext ctxt, BarListReq req) {
        BarListResp resp = new BarListResp();
        List<BarVO> list = barDAO.list(req.getSize(), req.getPageNum());
        for (BarVO vo : list) {
            BarDetailResp r = new BarDetailResp();
            BeanUtils.copyProperties(r, vo);
            r.setUserName(userService.getNickName(vo.getUserId()));
            resp.getData().add(r);
        }
        return resp;
    }

}
