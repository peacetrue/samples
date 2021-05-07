package com.github.peacetrue.sample.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

/**
 * @author : xiayx
 * @since : 2021-03-15 10:25
 **/
@Slf4j
public class HelloJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            String instanceId = context.getScheduler().getSchedulerInstanceId();
            log.info("\n\n{} execute job [{}]\n\n", instanceId, context);
        } catch (SchedulerException e) {
            e.printStackTrace();
            //watch com.github.peacetrue.sample.quartz.HelloJob * '{params,returnObj,throwExp}' -v -n 5 -x 3 '1==1'
            //watch com.github.peacetrue.sample.quartz.HelloJob execute '{params,returnObj,throwExp}' -v -n 5 -x 3 '1==1'
        }
    }

}
