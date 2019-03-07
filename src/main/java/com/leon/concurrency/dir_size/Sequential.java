package com.leon.concurrency.dir_size;

import java.io.File;

public class Sequential {

    public static void main(String[] args) {
        long start = System.nanoTime();
        long total = getTotalSizeOfFilesInDir(new File("C:\\Windows\\System32"));
        long stop = System.nanoTime();
        System.out.println("Total size: " + total + " - Time taken: " + ((stop - start) / 1.0e9));
    }

    private static long getTotalSizeOfFilesInDir(File file) {
        if (file.isFile()) {
            return file.length();
        }

        File[] files = file.listFiles();
        long total = 0;
        if (files != null) {
            for (File childFile : files) {
                total += getTotalSizeOfFilesInDir(childFile);
            }
        }

        return total;
    }

}
