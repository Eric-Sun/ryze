package com.j13.ryze.fetcher;

import com.j13.poppy.config.PropertiesConfiguration;
import com.j13.ryze.core.Logger;
import org.quartz.*;
import org.springframework.context.ApplicationContext;

public class ReplyDataInserterJob extends BaseJob {

    private int intervalMin;

    @Override
    public void init(PropertiesConfiguration configuration) {
        intervalMin = configuration.getIntValue("job.replyInsert.next.min");
        Logger.INSERTER.info("set job.replyInsert.next.min = {}", intervalMin);
    }

    @Override
    public void doExcute(ApplicationContext applicationContext,JobDataMap jdm) {
        DataInserter inserter = applicationContext.getBean(DataInserter.class);
        while (true) {
            Logger.INSERTER.info("reply data inserter start.");
            inserter.insertReplys();
            Logger.INSERTER.info("reply data inserter end.");
            try {
                Logger.INSERTER.info("reply data inserter will start in {} min", intervalMin);
                Thread.sleep(intervalMin * 60 * 1000);
            } catch (InterruptedException e) {
                Logger.INSERTER.error("", e);
            }
        }
    }

}
