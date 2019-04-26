package com.j13.ryze.destiny;

import com.j13.ryze.services.PostService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

public class PostOfflineVoteJob implements Job {
    private static Logger LOG = LoggerFactory.getLogger(PostOfflineVoteJob.class);


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SchedulerContext schCtx = null;
        try {
            schCtx = jobExecutionContext.getScheduler().getContext();
        } catch (SchedulerException e) {
            LOG.error(e.getMessage());
        }
        ApplicationContext applicationContext = (ApplicationContext)schCtx.get("applicationContext");

        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        int voteId = dataMap.getInt("voteId");
        int postId = dataMap.getInt("postId");
        LOG.info("post offline vote set result start. voteId={}", voteId);

        // 开启结束投票
        VoteService voteService = applicationContext.getBean(VoteService.class);
        PostService postService = applicationContext.getBean(PostService.class);
        int result = voteService.generateResult(voteId);
        if(result==DestinyConstants.Vote.Result.AGREE_WIN){
            postService.offline(postId);
        }
        LOG.info("post offline vote set result end. voteId={}", voteId);

    }
}
