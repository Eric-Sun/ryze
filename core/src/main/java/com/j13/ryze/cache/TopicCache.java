package com.j13.ryze.cache;

import com.alibaba.fastjson.JSON;
import com.j13.poppy.JedisManager;
import com.j13.ryze.core.Logger;
import com.j13.ryze.daos.TopicDAO;
import com.j13.ryze.vos.TopicVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TopicCache {


    @Autowired
    TopicDAO topicDAO;
    @Autowired
    JedisManager jedisManager;
    private static String KEY = "v10:topicInfo";

    @PostConstruct
    public void init() {
        List<TopicVO> list = topicDAO.listTopic();
        for (TopicVO vo : list) {
            jedisManager.set(KEY + ":" + vo.getId(), vo.getName());
            Logger.COMMON.info("add topic cache. id={},name={}", vo.getId(), vo.getName());
        }
    }


    public TopicVO get(int topicId) {
        String rawTopicObject = jedisManager.get(KEY + ":" + topicId);
        TopicVO vo = JSON.parseObject(rawTopicObject, TopicVO.class);
        return vo;
    }
}
