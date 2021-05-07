package com.github.peacetrue.sample.quartz;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.*;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author : xiayx
 * @since : 2021-03-15 10:40
 **/
@Slf4j
public class JDBCJobStoreH2Test {

    private Scheduler scheduler;

//    {
//        try {
////            scheduler = QuartzUtils.getH2Scheduler();
////            scheduler = QuartzUtils.getMysqlScheduler();
////            scheduler = QuartzUtils.getMysqlClusterScheduler();
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//        }
//    }

    public String reverseWord(String world) {
        String[] worlds = world.split(" +");
        return IntStream
                .range(0, worlds.length)
                .mapToObj(i -> worlds[worlds.length - 1 - i])
                .collect(Collectors.joining(" "));
    }

    @Test
    void reverseWordTest() {
        //用 java 写一个判断奇偶数的方法；2.用 java 写单例模式
        //问题：使用 java 实现一个在线秒级限流服务，只能使用 java 自带的API，不得使用第三方存储和Lib
        System.out.println(reverseWord("hello     world     java  "));
    }


    public static boolean isEven(int number) {
        return number % 2 == 0;
    }

    public static boolean isEvenFaster(int number) {
        // 10001
        // &
        // 00001
        // =
        // 00001
        return (number & 1) == 0;
    }


    @Data
    public static class GateWay {
        /** 每毫秒数的请求数量 */
        private Map<Long, Integer> counts = new HashMap<>();
        /** n 毫秒内最多 x 个请求 */
        private int milliSecond = 1_000;
        private int maxCount = 1_000_000;

        public String limit(String request) {
            synchronized (this) {
                long time = System.currentTimeMillis();
                long minTime = time - milliSecond;

                Integer count = counts.entrySet().stream()
                        .filter(entry -> entry.getKey() >= minTime)
                        .map(Map.Entry::getValue)
                        .reduce(0, Integer::sum);

                if (count >= maxCount) return fallback(request);

                counts.keySet().stream()
                        .filter(key -> key < minTime)
                        .forEach(counts::remove);
                counts.merge(time, 1, Integer::sum);
            }

            return handle(request);
        }

        public String handle(String request) {
            return "正常处理";
        }

        public String fallback(String request) {
            return "暂无资源";
        }
    }

//     public GateWay() {
//            this.clear();
//        }
//        public void clear() {
//            new Thread(() -> {
//                int differ = 1;
//                while (true) {
//                    long time = System.currentTimeMillis();
//                    long minTime = time - milliSecond - differ;
//                    counts.entrySet().stream()
//                            .filter(entry -> entry.getKey() < minTime)
//                            .forEach(entry -> counts.remove(entry.getKey()));
//                }
//            }).start();
//        }


    @Test
    void name() {
        GateWay gateWay = new GateWay();
        gateWay.setMaxCount(2);
        IntStream.range(0, 1000).forEach(value -> System.out.println(gateWay.limit("1")));
    }

    @Test
    void createJob() throws Exception {
        scheduler.addJob(QuartzUtils.helloJob(), true);
        Assertions.assertTrue(true);
    }

    @Test
    void scheduleJob() throws Exception {
        Date date = scheduler.scheduleJob(QuartzUtils.cronTrigger());
        Assertions.assertNotNull(date);
    }

    @Test
    void queryJob() throws Exception {
        Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyJobGroup());
        for (JobKey jobKey : jobKeys) {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            log.info("\n\njobDetail: {}\n", jobDetail);
            Assertions.assertNotNull(jobDetail);
        }
    }

    @Test
    void queryTrigger() throws Exception {
        Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
        for (TriggerKey triggerKey : triggerKeys) {
            Trigger trigger = scheduler.getTrigger(triggerKey);
            log.info("\n\ntrigger: {}\n", trigger);
            Assertions.assertNotNull(trigger);
        }
    }

    @Test
    void deleteJobs() throws Exception {
        scheduler.deleteJobs(new LinkedList<>(scheduler.getJobKeys(GroupMatcher.anyJobGroup())));
    }

    @Test
    void start() throws Exception {
        scheduler.start();
        LockSupport.park(this);
    }


}
