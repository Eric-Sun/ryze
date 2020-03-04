package com.j13.ryze.fetcher;

import com.j13.poppy.config.PropertiesConfiguration;
import com.j13.ryze.core.Logger;
import com.j13.ryze.destiny.VoteService;
import org.quartz.*;
import org.springframework.context.ApplicationContext;

public class TianyaFetcherJob extends BaseJob{

    private int intervalMin = 60;

    @Override
    public void init(PropertiesConfiguration configuration ) {
        intervalMin = configuration.getIntValue("job.fetch.next.min");
        Logger.FETCHER.info("set job.fetch.next.min = {}", intervalMin);
    }

    @Override
    public void doExcute(ApplicationContext applicationContext,JobDataMap jdm) {
        TianyaFetcher fetcher = applicationContext.getBean(TianyaFetcher.class);
        while (true) {
            Logger.FETCHER.info("tianya fetcher start.");
            fetcher.doFetch();
            Logger.FETCHER.info("tianya fetcher end.");
            try {
                Logger.FETCHER.info("tianya fetcher will start in {} min.", intervalMin);
                Thread.sleep(intervalMin * 60 * 1000);
            } catch (InterruptedException e) {
                Logger.INSERTER.error("", e);
            }
        }
    }

}
