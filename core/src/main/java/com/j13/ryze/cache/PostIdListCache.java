package com.j13.ryze.cache;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.j13.poppy.JedisManager;
import com.j13.ryze.daos.PostDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class PostIdListCache {
    private static Logger LOG = LoggerFactory.getLogger(PostIdListCache.class);


    @Autowired
    PostDAO postDAO;
    @Autowired
    JedisManager jedisManager;
    private static String KEY = "v10:postIdList";

    @PostConstruct
    public void init() {
        flush();
    }


    /**
     * 随机n个post
     *
     * @param count
     * @return
     */
    public List<String> randomNPostId(int count) {
        List<String> idStrList = jedisManager.srandmember(KEY, count);
        LOG.info("get postIds from cache({}), postIds={}", KEY, JSON.toJSONString(idStrList));
        return idStrList;
    }

    public void addPostId(int postId) {
        jedisManager.sadd(KEY, postId + "");
        LOG.info("add postId to cache({}), postId={}", KEY, postId);
    }

    public void removePostId(int postId) {
        jedisManager.srem(KEY, postId + "");
        LOG.info("remove postId to cache({}), postId={}", KEY, postId);

    }

    public void flush() {
        List<Integer> postIdList = postDAO.listPostIdForCache();
        List<String> strList = Lists.newLinkedList();
        for (Integer id : postIdList) {
            strList.add(id + "");
        }
        jedisManager.delete(KEY);
        jedisManager.sadd(KEY, strList.toArray(new String[0]));
        LOG.info("postIdList set in cache({}).", KEY);
    }
}
