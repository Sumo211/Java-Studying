package com.leon.experimental;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

class SyncPolling<T> implements Runnable {

    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private CountDownLatch latch = new CountDownLatch(1);

    private volatile boolean done;

    private long start = Instant.now().toEpochMilli(), end, pollDuration;

    private Supplier<T> pollMethod = null;

    private Predicate<T> verifier = null;

    SyncPolling<T> poll(int frequency, TimeUnit timeUnit) {
        this.pollDuration = TimeUnit.MILLISECONDS.convert(frequency, timeUnit);
        return this;
    }

    SyncPolling<T> atMost(int max, TimeUnit timeUnit) {
        this.end = start + TimeUnit.MILLISECONDS.convert(max, timeUnit);
        return this;
    }

    SyncPolling<T> method(Supplier<T> supplier) {
        this.pollMethod = supplier;
        return this;
    }

    SyncPolling<T> until(Predicate<T> predicate) {
        this.verifier = predicate;
        return this;
    }

    void execute() {
        executor.schedule(this, 0, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        T result = pollMethod.get();
        System.out.println(String.format("Polling %s at %s using %s", result, Instant.now(), Thread.currentThread().getName()));

        if (verifier.test(result)) {
            System.out.println(String.format("Completed at %s using %s", Instant.now(), Thread.currentThread().getName()));
            done = true;
            complete();
        } else if (start + pollDuration > end) {
            System.out.println(String.format("Terminated due to timeout at %s using %s", Instant.now(), Thread.currentThread().getName()));
            complete();
        } else {
            start = start + pollDuration;
            executor.schedule(this, pollDuration, TimeUnit.MILLISECONDS);
        }
    }

    void await() throws InterruptedException {
        latch.await();
    }

    boolean isDone() {
        return done;
    }

    private void complete() {
        latch.countDown();
        executor.shutdown();
    }

}
