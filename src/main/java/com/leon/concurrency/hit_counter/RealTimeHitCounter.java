package com.leon.concurrency.hit_counter;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * A hit counter for hits received in the past 5 minutes.
 * http://massivetechinterview.blogspot.com/2015/06/algorithm-how-to-count-number-of.html
 */
class RealTimeHitCounter {

    private static final int GRANULARITY = 300;

    private static volatile RealTimeHitCounter INSTANCE;

    private AtomicLongArray counter = new AtomicLongArray(GRANULARITY);

    private volatile int pos = 0;

    private RealTimeHitCounter() {
        PositionUpdater positionUpdater = new PositionUpdater(this);
        positionUpdater.start();
    }

    RealTimeHitCounter getInstance() {
        if (INSTANCE == null) {
            synchronized (RealTimeHitCounter.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RealTimeHitCounter();
                }
            }
        }

        return INSTANCE;
    }

    void hit() {
        counter.getAndIncrement(pos);
    }

    long get() {
        long total = 0;
        for (int i = 0; i < GRANULARITY; i++) {
            total += counter.get(i);
        }

        return total;
    }

    private void incrementPosition() {
        int index = (pos + 1) % GRANULARITY;
        counter.set(index, 0);
        pos = index;
    }

    static class PositionUpdater extends TimerTask {

        private static final int DELAY = 1000;

        private final RealTimeHitCounter hitCounter;

        private final Timer timer = new Timer(true);

        PositionUpdater(RealTimeHitCounter hitCounter) {
            this.hitCounter = hitCounter;
        }

        void start() {
            timer.schedule(this, DELAY);
        }

        @Override
        public void run() {
            hitCounter.incrementPosition();
        }

    }

}
