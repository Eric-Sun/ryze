package com.j13.ryze.utils;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class QuartzManager {
    private static Logger LOG = LoggerFactory.getLogger(QuartzManager.class);

    private Scheduler scheduler = null;

    @PostConstruct
    public void init() {
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
        try {
            Scheduler sched = schedFact.getScheduler();
            sched.start();
        } catch (SchedulerException e) {
            LOG.error(e.getMessage());
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            LOG.error(e.getMessage());
        }
    }


    public Scheduler getScheduler() {
        return scheduler;
    }
}
