package com.github.peacetrue.sample.quartz;

import org.junit.jupiter.api.Test;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author : xiayx
 * @since : 2021-03-15 10:40
 **/
public class JDBCJobStoreH2Test {

    /*
     *  Note, Quartz depends on row-level locking which means you must use the MVCC=TRUE
     *
     *
     */

    @Test
    void basic() throws Exception {
        StdSchedulerFactory fact = new StdSchedulerFactory();
        fact.initialize("quartz-h2.properties");
        Scheduler scheduler = fact.getScheduler();
        scheduler.start();

        Thread.sleep(1000L * 100);

        scheduler.shutdown();
    }

    @Test
    void scheduleJob() throws Exception {
        StdSchedulerFactory fact = new StdSchedulerFactory();
        fact.initialize("quartz-h2.properties");
        Scheduler scheduler = fact.getScheduler();
        scheduler.start();

        QuartzQuickStartGuideTest.scheduleJob(scheduler);
        Thread.sleep(1000 * 20L);

        scheduler.shutdown();

    }
}
