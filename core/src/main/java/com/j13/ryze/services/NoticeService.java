package com.j13.ryze.services;

import com.alibaba.fastjson.JSON;
import com.j13.ryze.core.Constants;
import com.j13.ryze.daos.NoticeDAO;
import com.j13.ryze.vos.NoticeVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {
    private static Logger LOG = LoggerFactory.getLogger(NoticeService.class);

    @Autowired
    NoticeDAO noticeDAO;


    /**
     * 添加通知，被回复内容为通知
     *
     * @param fromUserId
     * @param toUserId
     * @param replyId
     */
    public void addReplyNotice(int fromUserId, int toUserId, int targetReplyId, int replyId) {
        noticeDAO.add(fromUserId, toUserId, targetReplyId, replyId, Constants.NOTICE.TYPE.REPLY_NOTICE,
                Constants.NOTICE.STATUS.NOT_READ);
        LOG.info("add reply notice. fromUserId={},toUserId={},replyId={},targetReplyId={}",
                new Object[]{fromUserId, toUserId, replyId, targetReplyId});
    }


    /**
     * 添加通知，被回复的内容为帖子
     *
     * @param fromUserId
     * @param toUserId
     * @param postId
     */
    public void addPostNotice(int fromUserId, int toUserId, int postId, int replyId) {
        noticeDAO.add(fromUserId, toUserId, postId, replyId, Constants.NOTICE.TYPE.POST_NOTICE,
                Constants.NOTICE.STATUS.NOT_READ);
        LOG.info("add post notice. fromUserId={},toUserId={},postId={},replyId={}",
                new Object[]{fromUserId, toUserId, postId, replyId});
    }

    public void readNotice(int userId, int noticeId) {
        noticeDAO.updateStatus(userId, noticeId, Constants.NOTICE.STATUS.READED);
        LOG.info("read notice. noticeId={}", noticeId);
    }

    public void deleteNotice(int userId, int noticeId) {
        noticeDAO.delete(userId, noticeId);
        LOG.info("delete notice. noticeId={}", noticeId);
    }

    public List<NoticeVO> list(int userId) {
        List<NoticeVO> list = noticeDAO.list(userId);
        return list;
    }


}

