package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.AdminTopicAddReq;
import com.j13.ryze.api.req.AdminTopicDeleteReq;
import com.j13.ryze.api.req.AdminTopicListReq;
import com.j13.ryze.api.req.AdminTopicUpdateReq;
import com.j13.ryze.api.resp.AdminTopicDetailResp;
import com.j13.ryze.api.resp.AdminTopicListResp;
import com.j13.ryze.core.Logger;
import com.j13.ryze.daos.PostTopicDAO;
import com.j13.ryze.daos.TopicDAO;
import com.j13.ryze.vos.TopicVO;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminTopicFacade {

    @Autowired
    TopicDAO topicDAO;
    @Autowired
    PostTopicDAO postTopicDAO;

    @Action(name = "admin.topic.add")
    public CommonResultResp add(CommandContext ctxt, AdminTopicAddReq req) {
        int topicId = topicDAO.insert(req.getTopicName());
        Logger.COMMON.info("add topic. topicName=?, topicId=?", req.getTopicName(), topicId);
        return CommonResultResp.success();
    }


    @Action(name = "admin.topic.update")
    public CommonResultResp update(CommandContext ctxt, AdminTopicUpdateReq req) {
        topicDAO.updateName(req.getTopicId(), req.getTopicName());
        Logger.COMMON.info("update topic. topicName=?, topicId=?", req.getTopicName(), req.getTopicId());
        return CommonResultResp.success();
    }

    @Action(name = "admin.topic.list")
    public AdminTopicListResp list(CommandContext ctxt, AdminTopicListReq req) {
        AdminTopicListResp resp = new AdminTopicListResp();
        List<TopicVO> list = topicDAO.listTopic();
        Logger.COMMON.info("list topic. size=?" + list.size());
        for (TopicVO vo : list) {
            AdminTopicDetailResp detail = new AdminTopicDetailResp();
            BeanUtils.copyProperties(detail, vo);
            resp.getData().add(detail);
        }
        return resp;
    }

    @Action(name = "admin.topic.delete")
    public CommonResultResp delete(CommandContext ctxt, AdminTopicDeleteReq req) {
        // 先删除PostTopic相关的记录
        postTopicDAO.deleteByTopicId(req.getTopicId());
        // 删除topic信息
        topicDAO.deleteTopic(req.getTopicId());
        return CommonResultResp.success();
    }


}
