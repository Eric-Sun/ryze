package com.j13.ryze.fetcher;

import com.j13.ryze.core.Logger;
import com.j13.ryze.destiny.VoteService;
import org.quartz.*;
import org.springframework.context.ApplicationContext;

public class TianyaFetcherJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SchedulerContext schCtx = null;
        try {
            schCtx = jobExecutionContext.getScheduler().getContext();
        } catch (SchedulerException e) {
            Logger.FETCHER.error(e.getMessage());
        }
        ApplicationContext applicationContext = (ApplicationContext) schCtx.get("applicationContext");

        TianyaFetcher fetcher = applicationContext.getBean(TianyaFetcher.class);
        fetcher.doFetch();
        Logger.FETCHER.info("tianya fetcher started.");
    }
}
