package com.j13.ryze.fetcher;

import com.j13.poppy.config.PropertiesConfiguration;
import com.j13.ryze.core.Logger;
import com.j13.ryze.utils.QuartzManager;
import org.quartz.*;
import org.springframework.context.ApplicationContext;

import java.util.Properties;

public class PostDataInserterJob extends BaseJob {

    private int intervalMin;


    @Override
    public void init(PropertiesConfiguration configuration) {
        intervalMin = configuration.getIntValue("job.postInsert.next.min");
        Logger.INSERTER.info("set job.postInsert.next.min = {}", intervalMin);
    }

    @Override
    public void doExcute(ApplicationContext applicationContext,JobDataMap jdm) {

        DataInserter inserter = applicationContext.getBean(DataInserter.class);
        while (true) {
            Logger.INSERTER.info("post data inserter start.");
            inserter.insertPost();
            Logger.INSERTER.info("post data inserter end.");
            try {
                Logger.INSERTER.info("post data inserter will start in {} min", intervalMin);
                Thread.sleep(intervalMin * 60 * 1000);
            } catch (InterruptedException e) {
                Logger.INSERTER.error("", e);
            }
        }
    }

}
