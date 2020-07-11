package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.TopicListReq;
import com.j13.ryze.api.resp.AdminTopicDetailResp;
import com.j13.ryze.api.resp.AdminTopicListResp;
import com.j13.ryze.api.resp.TopicDetailResp;
import com.j13.ryze.api.resp.TopicListResp;
import com.j13.ryze.core.Logger;
import com.j13.ryze.daos.TopicDAO;
import com.j13.ryze.vos.TopicVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TopicFacade {

    @Autowired
    TopicDAO topicDAO;

    @Action(name="topic.list")
    public TopicListResp list(CommandContext ctxt, TopicListReq req){
        TopicListResp resp = new TopicListResp();
        List<TopicVO> list = topicDAO.listTopic();
        Logger.COMMON.info("list topic. size=" + list.size());
        for (TopicVO vo : list) {
            TopicDetailResp detail = new TopicDetailResp();
            BeanUtils.copyProperties(detail, vo);
            resp.getData().add(detail);
        }
        return resp;
    }
}
