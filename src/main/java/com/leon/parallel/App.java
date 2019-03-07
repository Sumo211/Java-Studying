package com.leon.parallel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

public class App {

    public static void main(String[] args) {
        try (InputStream in = new FileInputStream("C:\\Users\\ntcong\\Desktop\\sample-stops.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            csvStream(reader)
                    .skip(1)
                    .parallel()
                    .map(e -> String.join(",", e))
                    .forEach(System.out::println);

            batches(Files.lines(Paths.get("C:\\Users\\ntcong\\Desktop\\sample-stops.txt")).skip(1), 10).forEach(e -> System.out.println(e.size()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // https://www.airpair.com/java/posts/parallel-processing-of-io-based-data-with-java-streams
    private static Stream<String[]> csvStream(BufferedReader reader) {
        return stream(new CsvSpliterator(reader, 10), false).onClose(() -> {
            try {
                reader.close();
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        });
    }

    // https://stackoverflow.com/questions/30641383/java-8-stream-with-batch-processing
    private static <T> Stream<List<T>> batches(Stream<T> stream, int batchSize) {
        return batchSize <= 0
                ? Stream.of(stream.collect(toList()))
                : StreamSupport.stream(new BatchSpliterator<>(stream.spliterator(), batchSize), stream.isParallel());
    }

}
