package com.leon.concurrency;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class CounterTest {

    private ExecutorService service;

    private IntStream input;

    @Before
    public void init() {
        service = Executors.newFixedThreadPool(10);
        input = IntStream.range(0, 100);
    }

    @Test
    public void testUnsafeCounter() throws InterruptedException {
        UnsafeCounter unsafeCounter = new UnsafeCounter();

        input.forEach(index -> service.submit(unsafeCounter::increment));
        service.awaitTermination(100, TimeUnit.MILLISECONDS);

        assertEquals(100, unsafeCounter.getValue());
    }

    @Test
    public void testSafeCounterWithLock() throws InterruptedException {
        SafeCounterWithLock safeCounter = new SafeCounterWithLock();

        input.forEach(index -> service.submit(safeCounter::increment));
        service.awaitTermination(100, TimeUnit.MILLISECONDS);

        assertEquals(100, safeCounter.getValue());
    }

    @Test
    public void testAtomicCounter() throws InterruptedException {
        AtomicCounter atomicCounter = new AtomicCounter();

        input.forEach(index -> service.submit(atomicCounter::increment));
        service.awaitTermination(100, TimeUnit.MILLISECONDS);

        assertEquals(100, atomicCounter.getValue());
    }

}
