package com.j13.ryze.services;

import com.j13.poppy.JedisManager;
import com.j13.ryze.daos.ReplyDAO;
import com.j13.ryze.utils.CommonJedisManager;
import com.j13.ryze.vos.ReplyVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplyService {

    private static Logger LOG = LoggerFactory.getLogger(ReplyService.class);
    private static String SIMPLE_REPLY_CATALOG = "simplReply";
    @Autowired
    ReplyDAO replyDAO;
    @Autowired
    CommonJedisManager commonJedisManager;


    public ReplyVO getSimpleReply(int replyId) {
        ReplyVO vo = commonJedisManager.get(SIMPLE_REPLY_CATALOG, replyId, ReplyVO.class);
        if (vo == null) {
            LOG.debug("simpleReply not find in cache. catalog={},replyId={}", SIMPLE_REPLY_CATALOG, replyId);
            vo = replyDAO.get(replyId);
            commonJedisManager.set(SIMPLE_REPLY_CATALOG, replyId, vo);
            LOG.debug("simpleReply reset. catalog={},replyId={}", SIMPLE_REPLY_CATALOG, replyId);
        } else {
            LOG.debug("simpleReply exist. catalog={},replyId={}", SIMPLE_REPLY_CATALOG, replyId);
        }
        return vo;
    }
}
