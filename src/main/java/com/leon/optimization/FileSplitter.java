package com.leon.optimization;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileSplitter {

    private static final String TEMP_DIR = "C:/Users/ntcong/AppData/Local/Temp/testing/";

    private static final String PREFIX = "tmp";

    private static final String SUFFIX = ".csv";

    public static void main(String[] args) throws IOException {
        System.out.println(splitFile("F:/Transit Data/GTFS/dbahn/dbahn_hacon_gtfs_20180927/calendar_dates.txt", 50).size());
    }

    private static List<Path> splitFile(final String fileName, final int batchSize) throws IOException {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("batch size must be more than zero");
        }

        List<Path> partFiles = new ArrayList<>();
        final long sourceSize = Files.size(Paths.get(fileName));
        final long bytesPerSplit = 1024L * 1024L * batchSize;
        final long numSplits = sourceSize / bytesPerSplit;
        final long remainingBytes = sourceSize % bytesPerSplit;
        int position = 0;

        try (RandomAccessFile sourceFile = new RandomAccessFile(fileName, "r");
             FileChannel sourceChannel = sourceFile.getChannel()) {

            for (; position < numSplits; position++) {
                partFiles.add(writePartToFile(bytesPerSplit, position * bytesPerSplit, sourceChannel, PREFIX + position + SUFFIX));
            }

            if (remainingBytes > 0) {
                partFiles.add(writePartToFile(remainingBytes, position * bytesPerSplit, sourceChannel, PREFIX + position + SUFFIX));
            }
        }

        return partFiles;
    }

    private static Path writePartToFile(long byteSize, long position, FileChannel sourceChannel, String fileName) throws IOException {
        Path file = Paths.get(TEMP_DIR + fileName);
        try (RandomAccessFile toFile = new RandomAccessFile(file.toFile(), "rw");
             FileChannel toChannel = toFile.getChannel()) {
            sourceChannel.position(position);
            toChannel.transferFrom(sourceChannel, 0, byteSize);
        }

        return file;
    }

}
