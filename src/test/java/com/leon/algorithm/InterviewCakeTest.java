package com.leon.algorithm;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InterviewCakeTest {

    private InterviewCake interviewCake;

    private int[] input;

    @Before
    public void setUp() {
        interviewCake = new InterviewCake();
        input = new int[]{1, 2, 3, 1, 4};
    }

    @Test
    public void testGettingProductsOfAllIntsExceptAtIndex_OK() {
        int[] output = interviewCake.getProductsOfAllIntsExceptAtIndex(input);
        assertEquals(5, output.length);
        assertEquals(24, output[0]);
        assertEquals(12, output[1]);
        assertEquals(8, output[2]);
        assertEquals(24, output[3]);
        assertEquals(6, output[4]);
    }

    @Test
    public void testGettingMaxProfit_OK() {
        int output = interviewCake.getMaxProfit(input);
        assertEquals(3, output);
    }

    @Test
    public void testFindingFirstRepeatedNumber_OK() {
        int output = interviewCake.findFirstRepeatedNumber(input);
        assertEquals(1, output);
    }

    @Test
    public void testHighestProductOf3_OK() {
        int output = interviewCake.highestProductOf3(input);
        assertEquals(24, output);
    }

    @Test
    public void testCanTwoMoviesFillFlight_OK() {
        boolean output = interviewCake.canTwoMoviesFillFlight(input, 6);
        assertTrue(output);
    }

    @Test
    public void testFindingRotationPoint_OK() {
        String[] input = new String[]{"k", "v", "a", "b", "c", "d", "e", "g", "i"};
        int output = interviewCake.findRotationPoint(input);
        assertEquals(2, output);
    }

    @Test
    public void testCompressingURLList_OK() {
        InterviewCake.Trie root = new InterviewCake.Trie();
        assertTrue(root.checkPresentAndAdd("dog.com"));
        assertTrue(root.checkPresentAndAdd("dog.com/about"));
        assertFalse(root.checkPresentAndAdd("dog.com"));
        assertTrue(root.checkPresentAndAdd("dog.com/pug"));
        assertTrue(root.checkPresentAndAdd("dog.org"));
        assertTrue(root.checkPresentAndAdd("donut.net"));
        assertTrue(root.checkPresentAndAdd("dogood.org"));
    }

}
