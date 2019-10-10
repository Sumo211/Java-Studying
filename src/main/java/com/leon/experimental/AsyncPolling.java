package com.leon.experimental;

import java.time.Instant;
import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

class AsyncPolling<T> {

    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private int pollDuration, timeoutDuration;

    private TimeUnit pollTimeUnit, timeoutTimeUnit;

    private Supplier<T> pollMethod = null;

    private Predicate<T> verifier = null;

    AsyncPolling<T> poll(int frequency, TimeUnit timeUnit) {
        this.pollDuration = frequency;
        this.pollTimeUnit = timeUnit;
        return this;
    }

    AsyncPolling<T> atMost(int max, TimeUnit timeUnit) {
        this.timeoutDuration = max;
        this.timeoutTimeUnit = timeUnit;
        return this;
    }

    AsyncPolling<T> method(Supplier<T> supplier) {
        this.pollMethod = supplier;
        return this;
    }

    AsyncPolling<T> until(Predicate<T> predicate) {
        this.verifier = predicate;
        return this;
    }

    CompletableFuture<T> execute() {
        CompletableFuture<T> monitor = new CompletableFuture<>();

        ScheduledFuture<?> task = executor.scheduleAtFixedRate(() -> {
            T result = pollMethod.get();
            System.out.println(String.format("Polling %s at %s using %s", result, Instant.now(), Thread.currentThread().getName()));

            if (verifier.test(result)) {
                monitor.complete(result);
            }
        }, 0, pollDuration, pollTimeUnit);

        // timeout handling before Java 9
        /*executor.schedule(() -> {
            monitor.complete(null);
        }, timeoutDuration, timeoutTimeUnit);*/

        return monitor
                .orTimeout(timeoutDuration, timeoutTimeUnit)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        System.out.println(String.format("Terminated due to %s at %s using %s", throwable.getClass(), Instant.now(), Thread.currentThread().getName()));
                    } else {
                        System.out.println(String.format("Completed at %s using %s", Instant.now(), Thread.currentThread().getName()));
                    }

                    task.cancel(true);
                });
    }

}
