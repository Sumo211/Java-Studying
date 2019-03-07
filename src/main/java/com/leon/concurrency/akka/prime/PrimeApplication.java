package com.leon.concurrency.akka.prime;

import akka.actor.ActorSystem;
import akka.util.Timeout;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static akka.pattern.PatternsCS.ask;
import static akka.pattern.PatternsCS.pipe;

public class PrimeApplication {

    public static void main(String[] args) throws InterruptedException {
        final ActorSystem system = ActorSystem.create("FAIL");

        final int number = 10000000;
        final int partitions = 100;
        int chunksPerPartition = number / partitions;

        try {
            List<CompletableFuture<Object>> futures = new ArrayList<>();
            int lower, upper;
            for (int i = 0; i < partitions; i++) {
                lower = (i * chunksPerPartition) + 1;
                upper = (i == partitions - 1) ? number : (lower + chunksPerPartition - 1);
                futures.add(ask(
                        system.actorOf(Primes.props(String.valueOf(i))),
                        new Primes.RangeInput(lower, upper),
                        Timeout.create(Duration.ofMinutes(1)))
                .toCompletableFuture());
            }

            CompletableFuture<Integer> count = CompletableFuture.allOf(futures.toArray(new CompletableFuture[partitions]))
                    .thenApply(entry -> futures.stream()
                            .mapToInt(result -> (int) result.join())
                            .sum());

            pipe(count, system.dispatcher()).to(system.actorOf(Printer.props()));
            Thread.sleep(5000);
        } finally {
            system.terminate();
        }
    }

}
