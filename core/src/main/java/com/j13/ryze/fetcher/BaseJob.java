package com.j13.ryze.fetcher;

import com.j13.poppy.config.PropertiesConfiguration;
import com.j13.ryze.core.Logger;
import org.quartz.*;
import org.springframework.context.ApplicationContext;

public abstract class BaseJob implements Job {

    public abstract void init(PropertiesConfiguration configuration);

    public abstract void doExcute(ApplicationContext applicationContext );

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SchedulerContext schCtx = null;
        try {
            schCtx = jobExecutionContext.getScheduler().getContext();
        } catch (SchedulerException e) {
            Logger.FETCHER.error(e.getMessage());
        }
        ApplicationContext applicationContext = (ApplicationContext) schCtx.get("applicationContext");
        PropertiesConfiguration configuration = applicationContext.getBean(PropertiesConfiguration.class);
        init(configuration);
        doExcute(applicationContext);
    }
}
