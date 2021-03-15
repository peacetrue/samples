package com.github.peacetrue.sample.quartz;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * http://www.quartz-scheduler.org/documentation/quartz-2.3.0/quick-start.html
 *
 * @author : xiayx
 * @since : 2021-03-15 10:17
 **/
@Slf4j
public class QuartzQuickStartGuideTest {

    @Test
    void basic() {

        try {
            // Grab the Scheduler instance from the Factory
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            // and start it off
            scheduler.start();

            scheduler.shutdown();

        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    @Test
    void scheduleJob() {

        try {
            // Grab the Scheduler instance from the Factory
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            // and start it off
            scheduler.start();
            log.info("服务端启动完成---------------\n\n\n");

            scheduleJob(scheduler);

//            scheduler.shutdown();
            try {
                Thread.sleep(1000 * 20L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    public static Date scheduleJob(Scheduler scheduler) throws SchedulerException {

        // define the job and tie it to our HelloJob class
        JobDetail job = newJob(HelloJob.class)
                .withIdentity("job1", "group1")
                .build();

        // Trigger the job to run now, and then repeat every 40 seconds
        Trigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(5)
                        .repeatForever())
                .build();

        // Tell quartz to schedule the job using our trigger
        return scheduler.scheduleJob(job, trigger);

    }
}
