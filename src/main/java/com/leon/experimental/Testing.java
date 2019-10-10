package com.leon.experimental;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Testing {

    public static void main(String[] args) throws InterruptedException {
        /*String[] params = {
                "--read-pbf", "C:/tmp/switzerland-latest.osm.pbf",
                "--read-pbf", "C:/tmp/germany-latest.osm.pbf",
                "--merge", "--log-progress",
                "--write-pbf", "C:/tmp/tmp.osm"
        };

        Osmosis.run(params);

        System.out.println("Done");*/

        System.out.println("Start...");

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

        System.out.println("Finish...");
    }

}
