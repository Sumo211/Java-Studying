package com.leon.concurrency.priority;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class PriorityJobScheduler {

    private ExecutorService priorityJobPoolExecutor;

    private ExecutorService priorityJobScheduler = Executors.newSingleThreadExecutor();

    private PriorityBlockingQueue<Job> priorityQueue;

    public PriorityJobScheduler(int poolSize, int queueSize) {
        priorityJobPoolExecutor = Executors.newFixedThreadPool(poolSize);
        priorityQueue = new PriorityBlockingQueue<>(queueSize, Comparator.comparing(Job::getJobPriority));

        priorityJobScheduler.execute(() -> {
            while (true) {
                try {
                    priorityJobPoolExecutor.execute(priorityQueue.take());
                } catch (InterruptedException ex) {
                    System.err.println(ex.getMessage());
                    break;
                }
            }
        });
    }

    public void scheduleJob(Job job) {
        priorityQueue.add(job);
    }

    public int getQueuedTaskCount() {
        return priorityQueue.size();
    }

    public void close() {
        close(priorityJobPoolExecutor);
        close(priorityJobScheduler);
    }

    private void close(ExecutorService scheduler) {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
            scheduler.shutdownNow();
        }
    }

}
