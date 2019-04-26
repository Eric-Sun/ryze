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
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
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
    public int generateResult(int voteId) {
        VoteVO voteVO = voteDAO.getCount(voteId);
        if (voteVO.getAgreeCount() > voteVO.getDisagreeCount()) {
            voteDAO.setResult(voteId, DestinyConstants.Vote.Result.AGREE_WIN);
            LOG.info("generate result. voteId={},result={}", voteId, "AGREE_WIN");
            return DestinyConstants.Vote.Result.AGREE_WIN;
        } else {
            voteDAO.setResult(voteId, DestinyConstants.Vote.Result.DISAGREE_WIN);
            LOG.info("generate result. voteId={},result={}", voteId, "DISAGREE_WIN");
            return DestinyConstants.Vote.Result.DISAGREE_WIN;
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
            Date triggerDate = futureDate(1, DateBuilder.IntervalUnit.MINUTE);
            long triggerTime = triggerDate.getTime();
            int voteId = voteDAO.add(userId, resourceId, type, evidenceStr, triggerTime);

            Scheduler scheduler = quartzManager.getScheduler();

            JobDetail job = newJob(PostOfflineVoteJob.class)
                    .withIdentity("job_" + voteId, "group1")
                    .usingJobData("voteId", voteId)
                    .usingJobData("postId", resourceId)
                    .build();


            SimpleTrigger trigger = (SimpleTrigger) newTrigger()
                    .withIdentity("trigger_" + voteId, "group1")
                    .startAt(triggerDate) // use DateBuilder to create a date in the future
                    .forJob(job) // identify job with its JobKey
                    .build();
            try {
                scheduler.scheduleJob(job, trigger);
            } catch (SchedulerException e) {
                LOG.error(e.getMessage());
            }
            LOG.info("post offline vote quartz scheduled. voteId={}", voteId);

            return voteId;
        }
        return 0;
    }


    /**
     * 在系统重启的时候唤醒所有未完成的job，如果已经过期的话需要马上判定
     */
    public void awakeDeadJob() throws SchedulerException {

        List<VoteVO> list = voteDAO.getDeadJobs();
        LOG.info("dead job size = {}", list.size());
        for (VoteVO vo : list) {
            long triggertime = vo.getTriggertime();
            long currenttime = System.currentTimeMillis();
            if (currenttime > triggertime) {
                // 需要现在就触发
                Scheduler scheduler = quartzManager.getScheduler();
                JobDetail job = newJob(PostOfflineVoteJob.class)
                        .withIdentity("job_" + vo.getId(), "group1")
                        .usingJobData("voteId", vo.getId())
                        .usingJobData("postId", vo.getResourceId())
                        .build();


                SimpleTrigger trigger = (SimpleTrigger) newTrigger()
                        .withIdentity("trigger_" + vo.getId(), "group1")
                        .withSchedule(simpleSchedule())
                        .forJob(job) // identify job with its JobKey
                        .build();
                scheduler.scheduleJob(job, trigger);
            } else {
                // 需要设置未来的触发时间
                Scheduler scheduler = quartzManager.getScheduler();
                JobDetail job = newJob(PostOfflineVoteJob.class)
                        .withIdentity("job_" + vo.getId(), "group1")
                        .usingJobData("voteId", vo.getId())
                        .usingJobData("postId", vo.getResourceId())
                        .build();


                SimpleTrigger trigger = (SimpleTrigger) newTrigger()
                        .withIdentity("trigger_" + vo.getId(), "group1")
                        .startAt(new Date(triggertime)) // use DateBuilder to create a date in the future
                        .forJob(job) // identify job with its JobKey
                        .build();
                scheduler.scheduleJob(job, trigger);
            }
        }


    }
}
