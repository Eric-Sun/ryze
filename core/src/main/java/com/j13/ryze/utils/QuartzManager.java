package com.j13.ryze.utils;

import com.j13.ryze.destiny.VoteService;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class QuartzManager implements ApplicationContextAware {
    private static Logger LOG = LoggerFactory.getLogger(QuartzManager.class);

    private Scheduler scheduler = null;
    private ApplicationContext applicationContext = null;

    @Autowired
    VoteService voteService;

    @PostConstruct
    public void init() {
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
        try {
            scheduler = schedFact.getScheduler();
            scheduler.getContext().put("applicationContext", this.applicationContext);
            scheduler.start();
            LOG.info("quartz scheduler started.");
            awakeDeadJob();
            LOG.info("all dead jobs scheduler started.");

        } catch (SchedulerException e) {
            LOG.error(e.getMessage());
        }
    }

    /**
     * 重启的时候将会启动之前的所有关闭的job
     * 如果有需要判定的将会马上开始判定
     */
    public void awakeDeadJob() {
        try {
            voteService.awakeDeadJob();
        } catch (SchedulerException e) {
            LOG.error(e.getMessage());
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            scheduler.shutdown();
            LOG.info("quartz scheduler shutdown.");
        } catch (SchedulerException e) {
            LOG.error(e.getMessage());
        }
    }


    public Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
