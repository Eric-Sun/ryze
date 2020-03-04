package com.j13.ryze.fetcher;

import com.j13.poppy.config.PropertiesConfiguration;
import com.j13.ryze.core.Constants;
import com.j13.ryze.core.Logger;
import com.j13.ryze.daos.FPostDAO;
import com.j13.ryze.daos.FReplyDAO;
import com.j13.ryze.services.NoticeService;
import com.j13.ryze.services.PostService;
import com.j13.ryze.services.ReplyService;
import com.j13.ryze.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class DataInserter {

    @Autowired
    FPostDAO fPostDAO;
    @Autowired
    FReplyDAO fReplyDAO;
    @Autowired
    PostService postService;
    @Autowired
    ReplyService replyService;
    @Autowired
    UserService userService;
    @Autowired
    PropertiesConfiguration config;
    @Autowired
    NoticeService noticeService;

    private Random random = new Random();
    int ReplyInsertPerPostInsertSize;

    @PostConstruct
    public void init() {
        ReplyInsertPerPostInsertSize = config.getIntValue("job.replyInsert.perPost.replySize");
    }

    public void insertPost() {
        //插入一个post
        Logger.INSERTER.info("Post inserter start.");
        try {
            FPostVO fPostVO = fPostDAO.selectOneUninsertedPost();
            int userId = userService.randomMachineUser();
            int defaultBarId = config.getIntValue("default.bar.id");
            // 200304 修改为插入为下线状态
            int postId = postService.addOffLine(userId, defaultBarId, fPostVO.getTitle(), fPostVO.getContent(),
                    Constants.POST_ANONYMOUS.COMMON,
                    Constants.POST_TYPE.STORE, "[]");

            // 修改fpost的状态
            fPostDAO.updateStatusAndPostId(fPostVO.getId(), Constants.Fetcher.Status.PUSHED, postId, userId);
            Logger.INSERTER.info("insert post from fPostId={} to postId={}", fPostVO.getId(), postId);
        } catch (Exception e) {
            Logger.INSERTER.error("no post to insert.", e);
        }
        Logger.INSERTER.info("Post inserter end.");
    }

    public void insertReplys() {
//        插入所有的已插入post中的1-10随机的reply
        int defaultBarId = config.getIntValue("default.bar.id");

        Logger.INSERTER.info("Reply inserter start.");
        List<FPostVO> insertedPostList = fPostDAO.selectInsertedPostList();
        Collections.shuffle(insertedPostList);
        for (FPostVO vo : insertedPostList) {
            int replyCount = random.nextInt(ReplyInsertPerPostInsertSize) + 1;
            List<FReplyVO> fReplyVOList = fReplyDAO.findReplysByFPostId(vo.getSourcePostId(), replyCount);
            for (FReplyVO replyVO : fReplyVOList) {
                int randomUserId = 0;
                if (replyVO.getIsAuhtor() == 1) {
                    randomUserId = vo.getPostUserId();
                } else {
                    randomUserId = userService.randomMachineUser(vo.getPostUserId());
                }
                // 插入数据
                int replyId = 0;
                if (replyVO.getLastFReplyId() == 0) {
                    // 没有上一级
                    replyId = replyService.add(randomUserId, defaultBarId, vo.getPostId(), replyVO.getContent(),
                            Constants.REPLY_ANONYMOUS.COMMON, 0, "[]", false);
                    Logger.INSERTER.info("insert post from fReplyId={} to replyId={} on postId={} ", replyVO.getId(), replyId, vo.getPostId(), 0);

                    // 一级回复需要添加notice
                    if (replyVO.getIsAuhtor() == 1) {
                        noticeService.sendPostNotices(vo.getPostId());
                    }

                } else {
                    // 查找对应的lastFReplyId对应的replyId
                    int lastReplyId = fReplyDAO.findReplyIdFromFReplyId(replyVO.getLastFReplyId());
                    replyId = replyService.add(randomUserId, defaultBarId, vo.getPostId(), replyVO.getContent(),
                            Constants.REPLY_ANONYMOUS.COMMON, lastReplyId, "[]", false);
                    Logger.INSERTER.info("insert post from fReplyId={} to replyId={} on postId={}, lastReplyId={}", replyVO.getId(), replyId, vo.getPostId(), lastReplyId);
                }

                // 回补数据状态
                fReplyDAO.updateStatusAndReplyId(replyVO.getId(), Constants.Fetcher.Status.PUSHED, replyId);
                // 每次都刷，避免插入的数据是98，99，100，101条之类的只刷101的第二页
                try {
                    replyService.updateReplyListCache(vo.getPostId());
                } catch (Exception e) {
                    Logger.INSERTER.error(e.getMessage(), e);
                }
            }

        }
        Logger.INSERTER.info("Reply inserter end.");
    }


}
