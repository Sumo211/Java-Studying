package com.leon.algorithm;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CodilityTest {

    private Codility codility;

    private int[] input;

    @Before
    public void setUp() {
        codility = new Codility();
        input = new int[]{-1, 1, 4, 2, 6, 3};
    }

    @Test
    public void testFindingMissingIntegerNotOptimized_OK() {
        int output = codility.findMissingIntegerNotOptimized(input);
        assertEquals(5, output);
    }

    @Test
    public void testFindingMissingIntegerOptimized_OK() {
        int output = codility.findMissingIntegerOptimized(input);
        assertEquals(5, output);
    }

}
