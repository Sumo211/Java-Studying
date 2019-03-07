package com.leon.concurrency.dir_size;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ConcurrentWLatch {

    private final AtomicLong totalSize = new AtomicLong();

    private final AtomicLong pendingFileVisits = new AtomicLong();

    private final CountDownLatch latch = new CountDownLatch(1);

    private ExecutorService threadPool;

    public static void main(String[] args) throws InterruptedException {
        long start = System.nanoTime();
        long total = new ConcurrentWLatch().getTotalSizes(new File("C:\\Windows\\System32"));
        long stop = System.nanoTime();
        System.out.println("Total size: " + total + " - Time taken: " + ((stop - start) / 1.0e9));
    }

    private long getTotalSizes(File file) throws InterruptedException {
        threadPool = Executors.newFixedThreadPool(100);
        pendingFileVisits.incrementAndGet();
        try {
            updateTotalSizeOfFilesInDir(file);
            latch.await(100, TimeUnit.SECONDS);
            return totalSize.longValue();
        } finally {
            threadPool.shutdown();
        }
    }

    private void updateTotalSizeOfFilesInDir(File file) {
        long fileSize = 0;
        if (file.isFile()) {
            fileSize = file.length();
        } else {
            File[] files = file.listFiles();
            if (files != null) {
                for (File child : files) {
                    if (child.isFile()) {
                        fileSize += child.length();
                    } else {
                        pendingFileVisits.incrementAndGet();
                        threadPool.submit(() -> updateTotalSizeOfFilesInDir(child));
                    }
                }
            }
        }

        totalSize.addAndGet(fileSize);
        if (pendingFileVisits.decrementAndGet() == 0) {
            latch.countDown();
        }
    }

}
