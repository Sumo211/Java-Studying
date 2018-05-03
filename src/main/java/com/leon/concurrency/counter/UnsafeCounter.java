package com.leon.concurrency.counter;

public class UnsafeCounter implements Counter {

    private int counter;

    @Override
    public void increment() {
        this.counter++;
    }

    @Override
    public int getValue() {
        return this.counter;
    }

}
