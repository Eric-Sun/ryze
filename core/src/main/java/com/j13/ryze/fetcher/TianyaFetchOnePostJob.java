package com.j13.ryze.fetcher;

import com.j13.poppy.config.PropertiesConfiguration;
import com.j13.ryze.core.Logger;
import org.quartz.JobDataMap;
import org.springframework.context.ApplicationContext;

public class TianyaFetchOnePostJob extends BaseJob {


    @Override
    public void init(PropertiesConfiguration configuration) {
    }

    @Override
    public void doExcute(ApplicationContext applicationContext, JobDataMap jdm) {
        int postId = jdm.getIntValue("postId");
        TianyaFetcher fetcher = applicationContext.getBean(TianyaFetcher.class);
        Logger.FETCHER.info("tianya fetcher By PostId({}) start.", postId);
        fetcher.parsePostPage(postId, null);
        Logger.FETCHER.info("tianya fetcher By PostId({}) end.", postId);
    }
}
