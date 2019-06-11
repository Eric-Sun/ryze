package com.j13.ryze.fetcher;

import com.j13.poppy.config.PropertiesConfiguration;
import com.j13.ryze.core.Constants;
import com.j13.ryze.core.Logger;
import com.j13.ryze.daos.FPostDAO;
import com.j13.ryze.daos.FReplyDAO;
import com.j13.ryze.services.PostService;
import com.j13.ryze.services.ReplyService;
import com.j13.ryze.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private Random random = new Random();

    public void insertPost() {
        //插入一个post
        Logger.INSERTER.info("Post inserter start.");

        FPostVO fPostVO = fPostDAO.selectOneUninsertedPost();
        int userId = userService.randomMachineUser();
        int defaultBarId = config.getIntValue("default.bar.id");
        int postId = postService.add(userId, defaultBarId, fPostVO.getTitle(), fPostVO.getContent(),
                Constants.POST_ANONYMOUS.COMMON,
                Constants.POST_TYPE.DIARY, "[]");

        // 修改fpost的状态
        fPostDAO.updateStatusAndPostId(fPostVO.getId(), Constants.Fetcher.Status.PUSHED, postId, userId);
        Logger.INSERTER.info("insert post from fPostId={} to postId={}", fPostVO.getId(), postId);
        Logger.INSERTER.info("Post inserter end.");
    }

    public void insertReplys() {
//        插入所有的已插入post中的1-10随机的reply
        int defaultBarId = config.getIntValue("default.bar.id");

        Logger.INSERTER.info("Reply inserter start.");
        List<FPostVO> insertedPostList = fPostDAO.selectInsertedPostList();
        for (FPostVO vo : insertedPostList) {
            int replyCount = random.nextInt(5) + 1;
            List<FReplyVO> fReplyVOList = fReplyDAO.findReplysByFPostId(vo.getSourcePostId(), replyCount);
            for (FReplyVO replyVO : fReplyVOList) {
                int randomUserId = 0;
                if(replyVO.getIsAuhtor()==1){
                    randomUserId = vo.getPostUserId();
                }else{
                    randomUserId = userService.randomMachineUser(vo.getPostUserId());
                }
                // 插入数据
                int replyId = 0;
                if (replyVO.getLastFReplyId() == 0) {
                    // 没有上一级
                    replyId = replyService.add(randomUserId, defaultBarId, vo.getPostId(), replyVO.getContent(),
                            Constants.REPLY_ANONYMOUS.COMMON, 0, "[]",false);
                    Logger.INSERTER.info("insert post from fReplyId={} to replyId={} on postId={} ", replyVO.getId(), replyId, vo.getPostId(), 0);

                } else {
                    // 查找对应的lastFReplyId对应的replyId
                    int lastReplyId = fReplyDAO.findReplyIdFromFReplyId(replyVO.getLastFReplyId());
                    replyId = replyService.add(randomUserId, defaultBarId, vo.getPostId(), replyVO.getContent(),
                            Constants.REPLY_ANONYMOUS.COMMON, lastReplyId, "[]",false);
                    Logger.INSERTER.info("insert post from fReplyId={} to replyId={} on postId={}, lastReplyId={}", replyVO.getId(), replyId, vo.getPostId(), lastReplyId);


                }

                // 回补数据状态
                fReplyDAO.updateStatusAndReplyId(replyVO.getId(), Constants.Fetcher.Status.PUSHED, replyId);
            }
        }
        Logger.INSERTER.info("Reply inserter end.");
    }


}
