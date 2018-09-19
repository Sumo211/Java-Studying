package com.leon.concurrency.fork_join;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class RecursiveSolver extends RecursiveTask<Long> implements Solver {

    private final int[] input;

    public RecursiveSolver(int[] input) {
        this.input = input;
    }

    @Override
    public Long compute() {
        int nThreads = Runtime.getRuntime().availableProcessors();
        if (input.length > nThreads) {
            return ForkJoinTask.invokeAll(createSubTasks())
                    .stream()
                    .mapToLong(ForkJoinTask::join)
                    .sum();
        } else {
            return (long) Arrays.stream(input).sum();
        }
    }

    private Collection<RecursiveSolver> createSubTasks() {
        List<RecursiveSolver> dividedTasks = new ArrayList<>();
        int midpoint = input.length >>> 1;
        dividedTasks.add(new RecursiveSolver(Arrays.copyOfRange(input, 0, midpoint)));
        dividedTasks.add(new RecursiveSolver(Arrays.copyOfRange(input, midpoint, input.length)));
        return dividedTasks;
    }

}
