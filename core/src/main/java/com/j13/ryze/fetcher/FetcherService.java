package com.j13.ryze.fetcher;

import com.j13.poppy.config.PropertiesConfiguration;
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
        triggerFetcherJob(configuration.getIntValue("job.fetch.init.min"));
        triggerPostInserterJob(configuration.getIntValue("job.postInsert.init.min"));
        triggerReplyInserterJob(configuration.getIntValue("job.replyInsert.init.min"));
    }


    /**
     *
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


}
