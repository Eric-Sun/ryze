package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.exceptions.CommonException;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.AddTopicReq;
import com.j13.ryze.api.req.ListTopicReq;
import com.j13.ryze.api.req.TopicDeleteReq;
import com.j13.ryze.api.req.TopicUpdateContentReq;
import com.j13.ryze.api.resp.AddTopicResp;
import com.j13.ryze.api.resp.ListTopicResp;
import com.j13.ryze.api.resp.TopicDetailResp;
import com.j13.ryze.core.ErrorCode;
import com.j13.ryze.daos.BarDAO;
import com.j13.ryze.daos.BarMemberDAO;
import com.j13.ryze.daos.TopicDAO;
import com.j13.ryze.vos.TopicVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TopicFacade {

    @Autowired
    TopicDAO topicDAO;

    @Autowired
    BarDAO barDAO;

    @Autowired
    BarMemberDAO barMemberDAO;

    @Action(name = "topic.add", desc = "")
    public AddTopicResp add(CommandContext ctxt, AddTopicReq req) {
        AddTopicResp resp = new AddTopicResp();
        if (!barDAO.exist(req.getBarId())) {
            throw new CommonException(ErrorCode.Bar.NOT_EXIST);
        }

        if (!barMemberDAO.hasMember(req.getBarId(), req.getUserId())) {
            throw new CommonException(ErrorCode.Bar.NOT_HAS_MEMBER);
        }

        int id = topicDAO.add(req.getUserId(), req.getBarId(), req.getContent());
        resp.setTopicId(id);
        return resp;
    }

    @Action(name = "topic.list", desc = "")
    public ListTopicResp list(CommandContext ctxt, ListTopicReq req) {
        ListTopicResp resp = new ListTopicResp();
        if (!barDAO.exist(req.getBarId())) {
            throw new CommonException(ErrorCode.Bar.NOT_EXIST);
        }
        if (!barMemberDAO.hasMember(req.getBarId(), req.getUserId())) {
            throw new CommonException(ErrorCode.Bar.NOT_HAS_MEMBER);
        }

        List<TopicVO> list = topicDAO.list(req.getBarId(), req.getPageNum(), req.getSize());

        for (TopicVO vo : list) {
            TopicDetailResp r = new TopicDetailResp();
            BeanUtils.copyProperties(r, vo);
            resp.getList().add(r);
        }
        return resp;
    }

    @Action(name = "topic.updateContent")
    public CommonResultResp updateContent(CommandContext ctxt, TopicUpdateContentReq req) {
        return CommonResultResp.success();
    }


    @Action(name = "topic.delete")
    public CommonResultResp delete(CommandContext ctxt, TopicDeleteReq req) {
        return CommonResultResp.success();
    }
}
