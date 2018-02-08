package com.leon.benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

public class Loop {

    @Fork(2)
    @Measurement(iterations = 5)
    @Warmup(iterations = 5)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchForEachLoop(ExecutionPlan executionPlan) {
        int max = Integer.MIN_VALUE;
        for (int element : executionPlan.input) {
            max = Math.max(max, element);
        }
    }

    @Fork(2)
    @Measurement(iterations = 5)
    @Warmup(iterations = 5)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchForLoop(ExecutionPlan executionPlan) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < executionPlan.numOfElements; i++) {
            max = Math.max(max, executionPlan.input.get(i));
        }
    }

    @Fork(2)
    @Measurement(iterations = 5)
    @Warmup(iterations = 5)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchStream(ExecutionPlan executionPlan) {
        executionPlan.input.stream().mapToInt(Integer::intValue).max().getAsInt();
    }

    @Fork(2)
    @Measurement(iterations = 5)
    @Warmup(iterations = 5)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchParallelStream(ExecutionPlan executionPlan) {
        executionPlan.input.parallelStream().mapToInt(Integer::intValue).max().getAsInt();
    }

}
