package com.leon.benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@State(Scope.Benchmark)
public class ExecutionPlan {

    @Param({"1000000"})
    public int numOfElements;

    public List<Integer> input;

    @Setup(Level.Invocation)
    public void populateData() {
        input = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < numOfElements; i++) {
            input.add(random.nextInt(1000000));
        }
    }

}
