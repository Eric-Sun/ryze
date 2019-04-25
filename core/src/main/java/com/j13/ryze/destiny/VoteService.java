package com.j13.ryze.destiny;

import com.alibaba.fastjson.JSON;
import com.j13.ryze.core.Constants;
import com.j13.ryze.daos.VoteDAO;
import com.j13.ryze.services.PostService;
import com.j13.ryze.services.UserService;
import com.j13.ryze.utils.QuartzManager;
import com.j13.ryze.vos.PostVO;
import com.j13.ryze.vos.UserVO;
import com.j13.ryze.vos.VoteVO;
import org.quartz.DateBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Service
public class VoteService {

    private static Logger LOG = LoggerFactory.getLogger(VoteService.class);
    @Autowired
    VoteDAO voteDAO;
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    QuartzManager quartzManager;

    /**
     * 提交帖子下线的投票
     *
     * @param userId
     * @param postId
     * @return 投票id
     */
    public int createPostOfflineVote(int userId, int postId, PostOfflineVoteEvidence evidence) {
        String evidenceStr = JSON.toJSONString(evidence);
        int voteId = voteDAO.add(userId, postId, DestinyConstants.Vote.Type.POST_OFFLINE_VOTE, evidenceStr);
        LOG.info("add post offline vote. voteId={}", voteId);

        Scheduler scheduler = quartzManager.getScheduler();

        JobDetail job = newJob(PostOfflineVoteJob.class)
                .withIdentity("myJob", "group1")
                .build();


        SimpleTrigger trigger = (SimpleTrigger) newTrigger()
                .withIdentity("trigger5", "group1")
                .startAt(futureDate(5, DateBuilder.IntervalUnit.MINUTE)) // use DateBuilder to create a date in the future
                .forJob(job) // identify job with its JobKey
                .build();

        return voteId;
    }


    /**
     * 投赞成票
     *
     * @param voteId
     * @param userId
     */
    public void agreeOneVote(int voteId, int userId) {
        voteDAO.agree(voteId);
        LOG.info("agree one vote . voteId={},userId={}", voteId, userId);
    }

    /**
     * 投反对票
     *
     * @param voteId
     * @param userId
     */
    public void disagreeOneVote(int voteId, int userId) {
        voteDAO.disagree(voteId);
        LOG.info("disagree one vote . voteId={},userId={}", voteId, userId);
    }


    /**
     * 生成最终结果
     *
     * @param voteId
     */
    public void generateResult(int voteId) {
        VoteVO voteVO = voteDAO.getCount(voteId);
        if (voteVO.getAgreeCount() > voteVO.getDisagreeCount()) {
            voteDAO.setResult(voteId, DestinyConstants.Vote.Result.AGREE_WIN);
            LOG.info("generate result. voteId={},result={}", voteId, "AGREE_WIN");
        } else {
            voteDAO.setResult(voteId, DestinyConstants.Vote.Result.DISAGREE_WIN);
            LOG.info("generate result. voteId={},result={}", voteId, "DISAGREE_WIN");
        }
    }


    /**
     * 获取所有开始投票的列表
     *
     * @param pageNum
     * @param size
     * @return
     */
    public List<VoteVO> list(int pageNum, int size) {
        List<VoteVO> list = voteDAO.list(pageNum, size);
        for (VoteVO vo : list) {
            int voteUserId = vo.getUserId();
            UserVO voteUserVO = userService.getUserInfo(voteUserId);
            vo.setUserName(voteUserVO.getNickName());
            vo.setUserAvatarUrl(voteUserVO.getAvatarUrl());

            if (vo.getType() == DestinyConstants.Vote.Type.POST_OFFLINE_VOTE) {
                PostOfflineVoteEvidence evidence = JSON.parseObject(vo.getEvidence(), PostOfflineVoteEvidence.class);
                evidence.setPostId(vo.getResourceId());

                PostVO postVO = postService.getSimplePost(vo.getResourceId());
                UserVO postUserVO = userService.getUserInfo(postVO.getUserId());
                evidence.setUserId(postUserVO.getUserId());
                if (postVO.getAnonymous() == Constants.POST_ANONYMOUS.ANONYMOUS) {
                    evidence.setUserName(postUserVO.getNickName());
                    evidence.setUserAvatarUrl(postUserVO.getAvatarUrl());
                } else {
                    evidence.setUserName(postUserVO.getAnonNickName());
                    evidence.setUserAvatarUrl(postUserVO.getAnonLouUrl());
                }
                evidence.setTitle(postVO.getTitle());
                evidence.setCreatetime(postVO.getCreatetime());
                if (postVO.getContent().length() >= 20) {
                    evidence.setBriefContent(postVO.getContent().substring(0, 20));
                } else {
                    evidence.setBriefContent(postVO.getContent());
                }
                vo.setEvidenceObject(evidence);
            }
        }
        return list;
    }


    /**
     * 发起一个投票
     *
     * @param userId
     * @param resourceId
     * @param type
     * @param reason
     * @return
     */
    public int pushVote(int userId, int resourceId, int type, String reason) {
        if (type == DestinyConstants.Vote.Type.POST_OFFLINE_VOTE) {
            PostOfflineVoteEvidence evidence = new PostOfflineVoteEvidence();
            evidence.setReason(reason);
            String evidenceStr = JSON.toJSONString(evidence);
            int voteId = voteDAO.add(userId, resourceId, type, evidenceStr);
            return voteId;
        }
        return 0;
    }


}
