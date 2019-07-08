package com.j13.ryze.services;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.j13.ryze.core.Constants;
import com.j13.ryze.core.Logger;
import com.j13.ryze.daos.NoticeDAO;
import com.j13.ryze.vos.CollectionVO;
import com.j13.ryze.vos.NoticeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    @Autowired
    NoticeDAO noticeDAO;
    @Autowired
    CollectionService collectionService;


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
     *
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
     *
     * @param noticeId
     */
    public void updateUpdateTime(int noticeId) {
        noticeDAO.updateUpdateTime(noticeId);
    }


    /**
     * 给关注这个post的所有用户发送通知
     *
     * @param postId
     */
    public void sendPostNotices(int postId) {
        List<Integer> userIdList = Lists.newLinkedList();
        // 添加收藏了这个帖子的所有用户发通知
        List<CollectionVO> collectionVOList = collectionService.queryCollectionsByResourceId(postId, Constants.Collection.Type.POST);
        for (CollectionVO vo : collectionVOList) {
            // 检查是否已经有关于这个帖子和用户的未读通知，如果有的话就不插入了
            int noticeId = checkPostCollectionNoticeExist(vo.getUserId(), postId);
            if (noticeId == 0) {
                // 需要插入这个通知
                addPostCollctionNotice(vo.getUserId(), postId);
            } else {
                // 如果已经存在需要更新时间
                updateUpdateTime(noticeId);
            }
            userIdList.add(vo.getUserId());
        }
        Logger.COMMON.info("send post notices. postId={},userIdList={}", postId, JSON.toJSONString(userIdList));
    }
}

