package com.leon.concurrency.dir_size;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoin {

    private static final ForkJoinPool threadPool = new ForkJoinPool();

    public static void main(String[] args) {
        long start = System.nanoTime();
        long total = threadPool.invoke(new SizeFinder(new File("C:\\Windows\\System32")));
        long stop = System.nanoTime();
        System.out.println("Total size: " + total + " - Time taken: " + ((stop - start) / 1.0e9));
    }

    static class SizeFinder extends RecursiveTask<Long> {

        private final File file;

        SizeFinder(File file) {
            this.file = file;
        }

        @Override
        protected Long compute() {
            long totalSize = 0;
            if (file.isFile()) {
                totalSize = file.length();
            } else {
                File[] files = file.listFiles();
                if (files != null) {
                    List<ForkJoinTask<Long>> tasks = new ArrayList<>();
                    for (File child : files) {
                        if (child.isFile()) {
                            totalSize += child.length();
                        } else {
                            tasks.add(new SizeFinder(child));
                        }
                    }

                    for (ForkJoinTask<Long> task : invokeAll(tasks)) {
                        totalSize += task.join();
                    }
                }
            }

            return totalSize;
        }
    }

}
