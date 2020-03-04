package com.j13.ryze.fetcher;

import com.j13.poppy.config.PropertiesConfiguration;
import com.j13.ryze.core.Constants;
import com.j13.ryze.core.Logger;
import com.j13.ryze.destiny.PostOfflineVoteJob;
import com.j13.ryze.utils.QuartzManager;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.Date;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Service
public class FetcherService {

    @Autowired
    TianyaFetcher tianyaFetcher;

    @Autowired
    QuartzManager quartzManager;
    @Autowired
    PropertiesConfiguration configuration;


    @PostConstruct
    public void init() {
        if (configuration.getStringValue("job.fetch.switch").equals(Constants.Switch.ON)) {
            triggerFetcherJob(configuration.getIntValue("job.fetch.init.min"));
        } else {
            Logger.FETCHER.info("fetch job is off.");
        }
        if (configuration.getStringValue("job.postInsert.switch").equals(Constants.Switch.ON)) {
            triggerPostInserterJob(configuration.getIntValue("job.postInsert.init.min"));
        } else {
            Logger.FETCHER.info("postInsert job is off.");
        }
        if (configuration.getStringValue("job.replyInsert.switch").equals(Constants.Switch.ON)) {
            triggerReplyInserterJob(configuration.getIntValue("job.replyInsert.init.min"));
        } else {
            Logger.FETCHER.info("replyInsert job is off.");
        }
    }


    /**
     * @param min
     */
    public void triggerFetcherJob(int min) {
        // 初始化定时器，每隔10分钟抓取一次
        // 初始化后一分钟开始抓取
        Date triggerDate = futureDate(min, DateBuilder.IntervalUnit.MINUTE);
        Scheduler scheduler = quartzManager.getScheduler();

        JobDetail job = newJob(TianyaFetcherJob.class)
                .withIdentity("job_TianyaFetcher", "group1")
                .build();


        SimpleTrigger trigger = (SimpleTrigger) newTrigger()
                .withIdentity("trigger_TianyaFetcher", "group1")
                .startAt(triggerDate) // use DateBuilder to create a date in the future
                .forJob(job) // identify job with its JobKey
                .build();
        try {
            scheduler.scheduleJob(job, trigger);
            Logger.FETCHER.info("tianya fetcher will start in {} min later.", min);
        } catch (SchedulerException e) {
            Logger.FETCHER.error(e.getMessage());
        }

    }

    /**
     * 触发Post数据插入的任务
     */
    public void triggerPostInserterJob(int min) {
        Date triggerDate = futureDate(min, DateBuilder.IntervalUnit.MINUTE);
        Scheduler scheduler = quartzManager.getScheduler();

        JobDetail job = newJob(PostDataInserterJob.class)
                .withIdentity("job_post_data_inserter", "group1")
                .build();


        SimpleTrigger trigger = (SimpleTrigger) newTrigger()
                .withIdentity("trigger_post_data_inserter", "group1")
                .startAt(triggerDate) // use DateBuilder to create a date in the future
                .forJob(job) // identify job with its JobKey
                .build();
        try {
            scheduler.scheduleJob(job, trigger);
            Logger.INSERTER.info("Post Data Inserter will start in {} min later.", min);
        } catch (SchedulerException e) {
            Logger.FETCHER.error(e.getMessage());
        }
    }

    /**
     * 触发Post数据插入的任务
     */
    public void triggerReplyInserterJob(int min) {
        Date triggerDate = futureDate(min, DateBuilder.IntervalUnit.MINUTE);
        Scheduler scheduler = quartzManager.getScheduler();

        JobDetail job = newJob(ReplyDataInserterJob.class)
                .withIdentity("job_reply_data_inserter", "group1")
                .build();


        SimpleTrigger trigger = (SimpleTrigger) newTrigger()
                .withIdentity("trigger_reply_data_inserter", "group1")
                .startAt(triggerDate) // use DateBuilder to create a date in the future
                .forJob(job) // identify job with its JobKey
                .build();
        try {
            scheduler.scheduleJob(job, trigger);
            Logger.INSERTER.info("Reply Data Inserter will start in {} min later.", min);
        } catch (SchedulerException e) {
            Logger.FETCHER.error(e.getMessage());
        }
    }


    @PreDestroy
    public void destroy() {

    }


    public void triggerFetchTianyaByPostId(int postId) {
        Date triggerDate = futureDate(5, DateBuilder.IntervalUnit.SECOND);
        Scheduler scheduler = quartzManager.getScheduler();

        JobDetail job = newJob(TianyaFetchOnePostJob.class)
                .withIdentity("job_tianyaFetchOnePost" + postId, "group1")
                .build();

        job.getJobDataMap().put("postId", postId);

        SimpleTrigger trigger = (SimpleTrigger) newTrigger()
                .withIdentity("trigger_tianyaFetchOnePost" + postId, "group1")
                .startAt(triggerDate) // use DateBuilder to create a date in the future
                .forJob(job) // identify job with its JobKey
                .build();
        try {
            scheduler.scheduleJob(job, trigger);
            Logger.INSERTER.info("tianyaFetchOnePost(postId={}) will start in {} seconds later.", postId, 5);
        } catch (SchedulerException e) {
            Logger.FETCHER.error(e.getMessage());
        }
    }


}
