package com.leon.concurrency;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCounter implements Counter {

    private static final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void increment() {
        //counter.incrementAndGet();

        while (true) {
            int existingValue = getValue();
            int newValue = existingValue + 1;
            if (counter.compareAndSet(existingValue, newValue)) {
                return;
            }
        }
    }

    @Override
    public int getValue() {
        return counter.get();
    }

}
