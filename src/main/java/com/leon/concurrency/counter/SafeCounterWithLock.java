package com.leon.concurrency.counter;

public class SafeCounterWithLock implements Counter {

    private volatile int counter;

    @Override
    public synchronized void increment() {
        this.counter++;
    }

    @Override
    public synchronized  int getValue() {
        return this.counter;
    }

}
