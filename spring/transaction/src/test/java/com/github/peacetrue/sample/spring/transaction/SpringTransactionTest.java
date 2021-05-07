package com.github.peacetrue.sample.spring.transaction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * @author : xiayx
 * @since : 2021-03-04 09:14
 **/
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        TransactionAutoConfiguration.class,
        UserServiceImpl.class
})
class SpringTransactionTest {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private TransactionDefinition transactionDefinition;

    @Test
    void add() {
        User user = userService.add(new User("1", 1, "2"));
        Assertions.assertNotNull(user.getId());
        Long count = userService.count();
        Assertions.assertEquals(0, count);
    }

    @Test
    void getByIdForUpdate() throws Exception {
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        User user = userService.getByIdForUpdate(2L);
        LockSupport.park(this);
//        transactionManager.commit(transactionStatus);
//        Assertions.assertNotNull(user);
    }

    @Test
    void addByTransactionManager() {
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);

        User user = userService.add(new User("1", 1, "2"));
        Assertions.assertNotNull(user.getId());

        transactionManager.commit(transactionStatus);

        Long count = userService.count();
//        Assertions.assertEquals(1, count);
    }

    @Test
    void addByTransactionTemplate() throws Exception {
        User user = transactionTemplate.execute(transactionStatus -> userService.add(new User("1", 1, "2")));
        Assertions.assertNotNull(user);
        Long count = userService.count();
        Assertions.assertEquals(1, count);
    }

    @Test
    void addByTransactionTemplateRollback() throws Exception {
        User user = transactionTemplate.execute(transactionStatus -> {
            User user1 = userService.add(new User("1", 1, "2"));
            transactionStatus.setRollbackOnly();
            return user1;
        });
        Assertions.assertNotNull(user);
        Long count = userService.count();
        Assertions.assertEquals(0, count);
    }

    @Test
    void addCurrent() {
        int nThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        CountDownLatch latch = new CountDownLatch(nThreads);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(nThreads);
        boolean hasError[] = {false};

        for (int i = 0; i < nThreads; i++) {
            executorService.submit(() -> {
                TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
                try {
                    userService.add(new User("1", 1, "2"));
                } catch (Exception e) {
                    hasError[0] = true;
                }

                try {
                    cyclicBarrier.await();
                    if (hasError[0]) {
                        transactionManager.rollback(transactionStatus);
                    } else {
                        transactionManager.commit(transactionStatus);
                    }
                } catch (InterruptedException | BrokenBarrierException e) {
                    transactionManager.rollback(transactionStatus);
                }
                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(nThreads, userService.count());
    }

    @Test
    void addCurrent01() {
        int nThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        CountDownLatch latch = new CountDownLatch(nThreads);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(nThreads);
        AtomicInteger atomicInteger = new AtomicInteger(0);

        AtomicBoolean hasError = new AtomicBoolean(false);

        for (int i = 0; i < nThreads; i++) {
            executorService.submit(() -> {
                TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
                try {
                    userService.addWithError(new User("1", 1, "2"));
                } catch (Exception e) {
                    hasError.compareAndSet(false, true);
                }

                try {
                    cyclicBarrier.await();
                    if (hasError.get()) {
                        transactionManager.rollback(transactionStatus);
                    } else {
                        System.err.println(atomicInteger.getAndIncrement());
                        transactionManager.commit(transactionStatus);
                    }
                } catch (InterruptedException | BrokenBarrierException e) {
                    transactionManager.rollback(transactionStatus);
                }
                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(0, userService.count());
    }
}
