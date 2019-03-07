package com.leon.concurrency.dir_size;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class Concurrent {

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        long start = System.nanoTime();
        long total = getTotalSize(new File("C:\\Windows\\System32"));
        long stop = System.nanoTime();
        System.out.println("Total size: " + total + " - Time taken: " + ((stop - start) / 1.0e9));
    }

    private static long getTotalSize(File file) throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService threadPool = Executors.newFixedThreadPool(100);
        try {
            long total = 0;
            List<File> directories = new ArrayList<>();
            directories.add(file);

            while (!directories.isEmpty()) {
                List<Future<TotalFileSize>> partialResults = new ArrayList<>();
                for (File directory : directories) {
                    partialResults.add(threadPool.submit(() -> getTotalAndSubDir(directory)));
                }
                directories.clear();

                for (Future<TotalFileSize> task : partialResults) {
                    TotalFileSize result = task.get(100, TimeUnit.SECONDS);
                    total += result.size;
                    directories.addAll(result.subDir);
                }
            }

            return total;
        } finally {
            threadPool.shutdown();
        }
    }

    private static TotalFileSize getTotalAndSubDir(File file) {
        long total = 0;
        List<File> subDir = new ArrayList<>();

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File child : files) {
                    if (child.isFile()) {
                        total += child.length();
                    } else {
                        subDir.add(child);
                    }
                }
            }
        } else if (file.isFile()) {
            total += file.length();
        }

        return new TotalFileSize(total, subDir);
    }

    static class TotalFileSize {

        final long size;

        final List<File> subDir;

        TotalFileSize(long size, List<File> subDir) {
            this.size = size;
            this.subDir = Collections.unmodifiableList(subDir);
        }

    }

}
