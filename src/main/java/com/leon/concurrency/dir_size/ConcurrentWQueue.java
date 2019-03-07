package com.leon.concurrency.dir_size;

import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class ConcurrentWQueue {

    private final AtomicLong pendingFileVisits = new AtomicLong();

    private final BlockingQueue<Long> fileSizes = new ArrayBlockingQueue<>(500);

    private ExecutorService threadPool;

    public static void main(String[] args) throws InterruptedException {
        long start = System.nanoTime();
        long total = new ConcurrentWQueue().getTotalSizeOfFiles(new File("C:\\Windows\\System32"));
        long stop = System.nanoTime();
        System.out.println("Total size: " + total + " - Time taken: " + ((stop - start) / 1.0e9));
    }

    private long getTotalSizeOfFiles(File file) throws InterruptedException {
        threadPool = Executors.newFixedThreadPool(100);
        try {
            startExploringDir(file);
            long totalSize = 0;
            while (pendingFileVisits.get() > 0 || fileSizes.size() > 0) {
                totalSize += fileSizes.poll(10, TimeUnit.SECONDS);
            }

            return totalSize;
        } finally {
            threadPool.shutdown();
        }
    }

    private void startExploringDir(File file) {
        pendingFileVisits.incrementAndGet();
        threadPool.submit(() -> exploreDir(file));
    }

    private void exploreDir(File file) {
        long totalSize = 0;
        if (file.isFile()) {
            totalSize = file.length();
        } else {
            File[] files = file.listFiles();
            if (files != null) {
                for (File child : files) {
                    if (child.isFile()) {
                        totalSize += child.length();
                    } else {
                        startExploringDir(child);
                    }
                }
            }
        }

        try {
            fileSizes.put(totalSize);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        pendingFileVisits.decrementAndGet();
    }

}
