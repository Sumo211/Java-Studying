package com.leon.concurrency.dir_size;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class NaiveConcurrent {

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        long start = System.nanoTime();
        long total = getTotalSizeOfFile(new File("C:\\Windows\\System32"));
        long stop = System.nanoTime();
        System.out.println("Total size: " + total + " - Time taken: " + ((stop - start) / 1.0e9));
    }

    private static long getTotalSizeOfFile(File file) throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService threadPool = Executors.newFixedThreadPool(100);
        try {
            return getTotalSizeOfFilesInDir(threadPool, file);
        } finally {
            threadPool.shutdown();
        }
    }

    private static long getTotalSizeOfFilesInDir(ExecutorService threadPool, File file) throws InterruptedException, ExecutionException, TimeoutException {
        if (file.isFile()) {
            return file.length();
        }

        File[] files = file.listFiles();
        long total = 0;
        if (files != null) {
            List<Future<Long>> partialResults = new ArrayList<>();
            for (File child : files) {
                partialResults.add(threadPool.submit(() -> getTotalSizeOfFilesInDir(threadPool, child)));
            }

            for (Future<Long> task : partialResults) {
                total += task.get(100, TimeUnit.SECONDS);
            }
        }

        return total;
    }

}
