package com.leon.concurrency.prime;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

class ConcurrentPrimeFinder extends AbstractPrimeFinder {

    private final int poolSize;

    private final int numOfParts;

    private ConcurrentPrimeFinder(final int poolSize, final int numOfParts) {
        this.poolSize = poolSize;
        this.numOfParts = numOfParts;
    }

    @Override
    protected int countPrimes(final int number) {
        int count = 0;
        List<Callable<Integer>> partitions = new ArrayList<>();
        int chunksPerPartition = number / numOfParts;

        for (int i = 0; i < numOfParts; i++) {
            int lower = (i * chunksPerPartition) + 1;
            int upper = (i == (numOfParts - 1)) ? number : (lower + chunksPerPartition - 1);
            partitions.add(() -> countPrimesInRange(lower, upper));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);

        try {
            List<Future<Integer>> resultFromParts = executorService.invokeAll(partitions, 10000, TimeUnit.SECONDS);
            for (Future<Integer> result : resultFromParts) {
                count += result.get();
            }
        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException(ex);
        } finally {
            executorService.shutdown();
        }

        return count;
    }

    public static void main(String[] args) {
        ConcurrentPrimeFinder finder = new ConcurrentPrimeFinder(4, 1000);
        finder.timeAndCompute(10000000);
    }

}
