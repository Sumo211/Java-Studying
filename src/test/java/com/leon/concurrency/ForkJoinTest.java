package com.leon.concurrency;

import com.leon.concurrency.fork_join.LoopSolver;
import com.leon.concurrency.fork_join.RecursiveSolver;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;

import static org.junit.Assert.assertEquals;

public class ForkJoinTest {

    private static final int SIZE = 20000000;

    private static final int SEEK = 19580427;

    private static final int BOUND = 500000;

    private int[] input;

    @Before
    public void setup() {
        input = new int[SIZE];
        Random random = new Random(SEEK);
        for (int i = 0; i < SIZE; i++) {
            input[i] = random.nextInt(BOUND);
        }
    }

    @Test
    public void testSolver_OK() {
        long start_1 = System.nanoTime();
        LoopSolver loopSolver = new LoopSolver(input);
        long actualLoopSolverResult = loopSolver.compute();
        System.out.println("Loop: " + (System.nanoTime() - start_1));

        long start_2 = System.nanoTime();
        RecursiveSolver recursiveSolver = new RecursiveSolver(input);
        ForkJoinPool pool = ForkJoinPool.commonPool();
        long actualRecursiveSolverResult = pool.invoke(recursiveSolver);
        System.out.println("Recursive: " + (System.nanoTime() - start_2));

        assertEquals(actualLoopSolverResult, actualRecursiveSolverResult);
    }

}
