package com.leon.concurrency;

import org.junit.Before;
import org.junit.Test;

public class PriorityJobSchedulerTest {

    private int poolSize;

    private int queueSize;

    @Before
    public void setUp() {
        poolSize = 1;
        queueSize = 10;
    }

    @Test
    public void testWhenMultiplePriorityJobsQueued_ThenHighestPriorityJobIsPicked() {
        Job job1 = new Job("Job1", JobPriority.LOW);
        Job job2 = new Job("Job2", JobPriority.MEDIUM);
        Job job3 = new Job("Job3", JobPriority.HIGH);
        Job job4 = new Job("Job4", JobPriority.MEDIUM);
        Job job5 = new Job("Job5", JobPriority.LOW);
        Job job6 = new Job("Job6", JobPriority.HIGH);

        PriorityJobScheduler scheduler = new PriorityJobScheduler(poolSize, queueSize);
        scheduler.scheduleJob(job1);
        scheduler.scheduleJob(job2);
        scheduler.scheduleJob(job3);
        scheduler.scheduleJob(job4);
        scheduler.scheduleJob(job5);
        scheduler.scheduleJob(job6);

        while (scheduler.getQueuedTaskCount() != 0) ;

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(ex);
        }

        scheduler.close();

    }

}
