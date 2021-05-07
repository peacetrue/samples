package com.github.peacetrue.sample.quartz;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author : xiayx
 * @since : 2021-03-19 09:49
 **/
public abstract class QuartzUtils {

    public static Scheduler getH2Scheduler() throws SchedulerException {
        StdSchedulerFactory fact = new StdSchedulerFactory();
        fact.initialize("quartz-h2.properties");
        return fact.getScheduler();
    }

    public static Scheduler getMysqlScheduler() throws SchedulerException {
        StdSchedulerFactory fact = new StdSchedulerFactory();
        fact.initialize("quartz-mysql.properties");
        return fact.getScheduler();
    }
    public static Scheduler getMysqlClusterScheduler() throws SchedulerException {
        StdSchedulerFactory fact = new StdSchedulerFactory();
        fact.initialize("quartz-mysql-cluster.properties");
        return fact.getScheduler();
    }

    public static JobDetail helloJob() {
        return newJob(HelloJob.class)
                .withIdentity("job1", "group1")
                .storeDurably()
                .build();
    }

    public static Trigger simpleTrigger() {
        return newTrigger()
                .withIdentity("simpleTrigger", "group1").startNow()
                .withSchedule(simpleSchedule().withIntervalInSeconds(5).repeatForever())
                .build();
    }

    public static Trigger cronTrigger() {
        return newTrigger()
                .withIdentity("cronTrigger", "group1")
                .withSchedule(cronSchedule("* * * * * ?"))
                .forJob(QuartzUtils.helloJob().getKey())
                .build();
    }

}
