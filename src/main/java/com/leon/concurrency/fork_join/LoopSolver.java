package com.leon.concurrency.fork_join;

public class LoopSolver implements Solver {

    private final int[] input;

    public LoopSolver(int[] input) {
        this.input = input;
    }

    @Override
    public Long compute() {
        long result = 0;
        for (int element : input) {
            result += element;
        }

        return result;
    }

}
