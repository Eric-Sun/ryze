package com.j13.ryze.fetcher;

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


    @PostConstruct
    public void init() {
        // 初始化定时器，每隔10分钟抓取一次
        // 初始化后一分钟开始抓取
        Date triggerDate = futureDate(1, DateBuilder.IntervalUnit.MINUTE);
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
            Logger.FETCHER.info("tianya fetcher will start in 1 min later.");
        } catch (SchedulerException e) {
            Logger.FETCHER.error(e.getMessage());
        }

    }

    @PreDestroy
    public void destroy() {

    }


}
