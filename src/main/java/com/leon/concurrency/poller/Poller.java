package com.leon.concurrency.poller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Poller {

    private static final long FETCH_INTERVAL = 10 * 1000;

    private static final long WAIT_LIMIT = 5 * 1000;

    private boolean fetch = true;

    void start() {
        ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
        es.scheduleAtFixedRate(this::scheduleFetch, FETCH_INTERVAL, FETCH_INTERVAL, TimeUnit.MILLISECONDS);
    }

    private synchronized void scheduleFetch() {
        fetch = true;
        notify();
    }

    <T> T poll() throws InterruptedException {
        return awaitFetch() ? doFetch() : null;
    }

    private synchronized boolean awaitFetch() throws InterruptedException {
        if (!fetch) {
            wait(WAIT_LIMIT);
        }

        try {
            return fetch;
        } finally {
            fetch = false;
        }
    }

    private <T> T doFetch() {
        // fetching logic
        return null;
    }

}
