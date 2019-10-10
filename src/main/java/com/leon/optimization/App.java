package com.leon.optimization;

import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class App {

    private static final String BUNDLE_ID = "dbahn";

    private static final int CHUNK_SIZE = 1000000;

    public static void main(String[] args) {
        try {
            final File source = new File("F:/Transit Data/GTFS/dbahn/dbahn_hacon_gtfs_20180927/calendar_dates.txt");
            final List<String> requiredCols = Arrays.asList("service_id", "date", "exception_type");
            System.out.println("Start importing...");

            Instant startSplitting = Instant.now();
            CompletableFuture<List<File>> futureInputs = CompletableFuture.supplyAsync(() -> splitFile(source, requiredCols));
            List<File> childFiles = futureInputs.get();
            System.out.println(childFiles.size() + " - " + Duration.between(startSplitting, Instant.now()).getSeconds());

            Instant startImporting = Instant.now();
            final Executor exclusiveExecutor = Executors.newFixedThreadPool(8);
            List<CompletableFuture<Long>> futureResults = new ArrayList<>();
            for (File child : childFiles) {
                futureResults.add(CompletableFuture.supplyAsync(() -> importUsingFile(child, requiredCols), exclusiveExecutor));
            }

            CompletableFuture.allOf(futureResults.toArray(new CompletableFuture[0])).join();

            //CompletableFuture.supplyAsync(() -> importUsingMemory(source, requiredCols)).get();

            System.out.println("Done in: " + Duration.between(startImporting, Instant.now()).getSeconds());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @SneakyThrows(value = {SQLException.class, IOException.class})
    private static long importUsingMemory(File source, List<String> requiredCols) {
        Connection connection = CustomDataSource.getConnection();

        try (connection; BufferedReader reader = new BufferedReader(new FileReader(source))) {
            CopyManager copier = new CopyManager(connection.unwrap(BaseConnection.class));
            long copied = 0;
            String copyQuery = String.format("COPY %s.%s (%s) FROM STDIN CSV HEADER", "ingestor", "gtfs_calendar_dates", getHeaders(requiredCols));
            int counter = 0;
            StringBuilder builder = new StringBuilder();
            for (CSVRecord record : CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {
                builder.append("dbahn");
                for (String col : requiredCols) {
                    builder.append(",");
                    try {
                        builder.append(record.get(col));
                    } catch (IllegalArgumentException ex) {
                        // ignore
                    }
                }
                builder.append("\n");
                counter++;

                if (++counter % CHUNK_SIZE == 0) {
                    copied += copier.copyIn(copyQuery, new ByteArrayInputStream(builder.toString().getBytes()));
                    builder.delete(0, builder.length());
                }
            }

            copied += copier.copyIn(copyQuery, new ByteArrayInputStream(builder.toString().getBytes()));
            return copied;
        }
    }

    @SneakyThrows(value = {SQLException.class, IOException.class})
    private static long importUsingFile(File source, List<String> requiredCols) {
        Connection connection = CustomDataSource.getConnection();

        try (connection; BufferedReader reader = new BufferedReader(new FileReader(source))) {
            CopyManager copier = new CopyManager(connection.unwrap(BaseConnection.class));
            long copied;
            String copyQuery = String.format("COPY %s.%s (%s) FROM STDIN CSV HEADER", "ingestor", "gtfs_calendar_dates", getHeaders(requiredCols));
            copied = copier.copyIn(copyQuery, reader);
            System.out.println("Finished " + source.getName() + " at " + LocalDateTime.now());

            return copied;
        }
    }

    private static List<File> splitFile(File source, List<String> requiredCols) {
        List<File> childFiles = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
            int counter = 0;
            List<List<Object>> temp = new ArrayList<>(CHUNK_SIZE);

            for (CSVRecord record : CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {
                temp.add(extractRequiredCols(record, requiredCols));

                if (++counter % CHUNK_SIZE == 0) {
                    childFiles.add((makeChildFile(source.getName(), getHeaders(requiredCols), temp)));
                    temp.clear();
                }
            }

            childFiles.add((makeChildFile(source.getName(), getHeaders(requiredCols), temp)));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return childFiles;
    }

    private static List<Object> extractRequiredCols(CSVRecord record, List<String> requiredCols) {
        List<Object> result = new ArrayList<>();
        result.add(BUNDLE_ID);

        for (String col : requiredCols) {
            try {
                result.add(record.get(col));
            } catch (IllegalArgumentException ex) {
                result.add(null);
            }
        }

        return result;
    }

    private static File makeChildFile(String fileName, String headers, List<List<Object>> values) throws IOException {
        Path childFile = Files.createTempFile("dbahn", fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(childFile);
             CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers))) {
            printer.printRecords(values);
            printer.flush();
        }

        return childFile.toFile();
    }

    private static String getHeaders(List<String> requiredCols) {
        return "bundle_id," + String.join(",", requiredCols);
    }

}
