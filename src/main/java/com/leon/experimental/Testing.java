package com.leon.experimental;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Testing {

    public static void main(String[] args) throws IOException {
        /*String[] params = {
                "--read-pbf", "C:/tmp/germany-latest.osm.pbf",
                "--read-pbf", "C:/tmp/alps-latest.osm.pbf",
                "--merge", "--log-progress",
                "--write-pbf", "C:/tmp/germany-alps.osm.pbf"
        };

        Osmosis.run(params);

        System.out.println("Done");*/

        /*System.out.println("Start...");

        AsyncPolling<Integer> asyncPolling = new AsyncPolling<>();

        CompletableFuture<Integer> result = asyncPolling
                .poll(5, TimeUnit.SECONDS)
                .method(() -> ThreadLocalRandom.current().nextInt(1, 6))
                .until(num -> num == 3)
                .atMost(15, TimeUnit.SECONDS)
                .execute();

        result.thenAccept(System.out::println);

        SyncPolling<Integer> syncPolling = new SyncPolling<>();

        syncPolling.poll(5, TimeUnit.SECONDS)
                .method(() -> ThreadLocalRandom.current().nextInt(1, 6))
                .until(num -> num == 3)
                .atMost(15, TimeUnit.SECONDS)
                .execute();

        syncPolling.await();

        System.out.println(syncPolling.isDone());

        System.out.println("Finish...");*/

        /*System.out.println("Starting...");
        long start = Instant.now().toEpochMilli();
        File input = new File("C:\\tmp\\v2_shapes\\v2_shapes.csv");
        List<String> updated = new ArrayList<>();
        String replacedValue = getReplacedValue(input.toPath(), "inventory_id");

        try (LineIterator lineIterator = FileUtils.lineIterator(input, "UTF-8");) {
            updated.add(lineIterator.next()); //ignore header

            while (lineIterator.hasNext()) {
                updated.add(lineIterator.next().replace(replacedValue, "1181"));
            }
        }

        Files.write(Paths.get("C:\\tmp\\v2_shapes\\updated_v2_shapes.csv"), updated, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
        System.out.println("Took " + (Instant.now().toEpochMilli() - start));*/
    }

    private static String getReplacedValue(Path filePath, String replacedField) throws IOException {
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            List<String> tmp = lines.limit(2).collect(toList());

            String[] headers = tmp.get(0).split(",");
            String[] values = tmp.get(1).split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            if (headers.length == values.length) {
                Map<String, String> simpleView = new LinkedHashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    simpleView.put(headers[i], values[i]);
                }

                return simpleView.get(replacedField);
            }

            return "";
        }
    }

}