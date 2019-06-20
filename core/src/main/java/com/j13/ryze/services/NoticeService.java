package com.j13.ryze.services;

import com.alibaba.fastjson.JSON;
import com.j13.ryze.core.Constants;
import com.j13.ryze.core.Logger;
import com.j13.ryze.daos.NoticeDAO;
import com.j13.ryze.vos.NoticeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

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
        Logger.COMMON.info("add reply notice. fromUserId={},toUserId={},replyId={},targetReplyId={}",
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
        Logger.COMMON.info("add post notice. fromUserId={},toUserId={},postId={},replyId={}",
                new Object[]{fromUserId, toUserId, postId, replyId});
    }

    public void readNotice(int userId, int noticeId) {
        noticeDAO.updateStatus(userId, noticeId, Constants.NOTICE.STATUS.READED);
        Logger.COMMON.info("read notice. noticeId={}", noticeId);
    }

    public void deleteNotice(int userId, int noticeId) {
        noticeDAO.delete(userId, noticeId);
        Logger.COMMON.info("delete notice. noticeId={}", noticeId);
    }

    public List<NoticeVO> list(int userId) {
        List<NoticeVO> list = noticeDAO.list(userId);
        return list;
    }


    public int listNotReadSize(int userId) {
        return noticeDAO.listNotReadSize(userId);
    }

    public void readAll(int uid) {
        noticeDAO.readAll(uid);
        Logger.COMMON.info("read all notices. userId={}", uid);
    }


    /**
     * 查询帖子收藏通知的id，如果id为0，说明不存在这个通知
     *
     * @param userId
     * @param postId
     * @return
     */
    public int checkPostCollectionNoticeExist(int userId, int postId) {
        return noticeDAO.getPostCollectionNoticeId(userId, postId);
    }

    /**
     * 添加一个PostCollection的通知
     * @param userId
     * @param postId
     */
    public void addPostCollctionNotice(int userId, int postId) {
        noticeDAO.add(Constants.NOTICE.POST_COLLECTION_FROM_USER_ID, userId, postId,
                Constants.NOTICE.POST_COLLECTION_REPLY_ID, Constants.NOTICE.TYPE.POST_COLLECTION_NEW_INFO,
                Constants.NOTICE.STATUS.NOT_READ);
        Logger.COMMON.info("add post collection notice. toUserId={},postId={}",
                new Object[]{userId, postId});
    }

    /**
     * 刷新通知的更新时间
     * @param noticeId
     */
    public void updateUpdateTime(int noticeId) {
        noticeDAO.updateUpdateTime(noticeId);
    }
}

