package com.leon.concurrency.tmp;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 1. Preserve Invariant - Fixing constructor
 * 2. Mind Your Resources - Using a periodic task scheduler ( timer )
 * 3. Ensure Visibility - Crossing the memory barrier
 * 4. Enhance Concurrency - Introducing lock objects
 * 5. Ensure Atomicity - Explicit synchronization
 */
public class EnergySource {

    private final long MAX_LEVEL = 100;

    private long level = MAX_LEVEL;

    private long usage = 0;

    private final ReentrantReadWriteLock monitor = new ReentrantReadWriteLock();

    private static final ScheduledExecutorService replenishTimer = Executors.newScheduledThreadPool(10,
            runnable -> {
                Thread thread = new Thread(runnable);
                thread.setDaemon(true);
                return thread;
            });

    private ScheduledFuture<?> replenishTask;

    private EnergySource() {

    }

    public static EnergySource create() {
        final EnergySource energySource = new EnergySource();
        energySource.init();
        return energySource;
    }

    public long getUnitsAvailable() {
        monitor.readLock().lock();
        try {
            return level;
        } finally {
            monitor.readLock().unlock();
        }
    }

    public long getUsageCount() {
        monitor.readLock().lock();
        try {
            return usage;
        } finally {
            monitor.readLock().unlock();
        }
    }

    public boolean useEnergy(long units) {
        monitor.writeLock().lock();
        try {
            if (units > 0 && level >= units) {
                level -= units;
                usage++;
                return true;
            }

            return false;
        } finally {
            monitor.writeLock().unlock();
        }
    }

    public synchronized void stopEnergySource() {
        replenishTask.cancel(false);
    }

    private void init() {
        replenishTask = replenishTimer.scheduleAtFixedRate(this::replenish, 0, 1, TimeUnit.SECONDS);
    }

    private void replenish() {
        monitor.writeLock().lock();
        try {
            if (level < MAX_LEVEL) {
                level++;
            }
        } finally {
            monitor.writeLock().unlock();
        }
    }

}
