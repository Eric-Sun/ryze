package com.j13.ryze.services;

import com.j13.ryze.daos.PostDAO;
import com.j13.ryze.daos.ReplyDAO;
import com.j13.ryze.utils.CommonJedisManager;
import com.j13.ryze.vos.PostVO;
import com.j13.ryze.vos.ReplyVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private static Logger LOG = LoggerFactory.getLogger(PostService.class);
    private static String SIMPLE_POST_CATALOG = "simplePost";

    @Autowired
    ReplyDAO replyDAO;
    @Autowired
    CommonJedisManager commonJedisManager;
    @Autowired
    PostDAO postDAO;

    public int replyCount(int postId) {
        int count = replyDAO.replyCount(postId);
        return count;
    }

    public PostVO getSimplePost(int postId) {
        PostVO vo = commonJedisManager.get(SIMPLE_POST_CATALOG, postId, PostVO.class);
        if (vo == null) {
            LOG.debug("simplePost not find in cache. catalog={},postId={}", SIMPLE_POST_CATALOG, postId);
            vo = postDAO.get(postId);
            commonJedisManager.set(SIMPLE_POST_CATALOG, postId, vo);
            LOG.debug("simplePost reset. catalog={},postId={}", SIMPLE_POST_CATALOG, postId);
        } else {
            LOG.debug("simplePost exist. catalog={},postId={}", SIMPLE_POST_CATALOG, postId);
        }
        return vo;

    }
}
