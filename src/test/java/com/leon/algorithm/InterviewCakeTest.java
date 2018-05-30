package com.leon.algorithm;

import com.leon.structure.BinaryTreeNode;
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

    @Test
    public void testFindingRectangleOverlap_OK() {
        Rectangle rec1 = new Rectangle(1, 1, 6, 3);
        Rectangle rec2 = new Rectangle(5, 2, 3, 6);

        Rectangle output = Rectangle.findRectangleOverlap(rec1, rec2);
        assertEquals(5, output.getLeftX());
        assertEquals(2, output.getBottomY());
        assertEquals(2, output.getWidth());
        assertEquals(2, output.getHeight());
    }

    @Test
    public void testFindingUniqueDeliveryId_OK() {
        int[] input = {1, 3, 2, 1, 2};
        int output = interviewCake.findUniqueDeliveryId(input);
        assertEquals(3, output);
    }

    @Test
    public void testMergingTwoSortedArrays_OK() {
        int[] firstArray = new int[]{3, 4, 6, 10, 11, 15};
        int[] secondArray = new int[]{1, 5, 8, 12, 14, 19};

        int[] expected = new int[]{1, 3, 4, 5, 6, 8, 10, 11, 12, 14, 15, 19};
        assertArrayEquals(expected, interviewCake.mergeArrays(firstArray, secondArray));
    }

    @Test
    public void testIsBracketValid_OK() {
        String input = "{ [ ( ] ) }";
        assertFalse(interviewCake.isBracketValid(input));
    }

    @Test
    public void testReverseWords_OK() {
        char[] input = {'c', 'a', 'k', 'e', ' ',
                'p', 'o', 'u', 'n', 'd', ' ',
                's', 't', 'e', 'a', 'l'};

        interviewCake.reserveWords(input);
        assertEquals("steal pound cake", new String(input));
    }

    @Test
    public void testIsBinarySearchTree_OK() {
        BinaryTreeNode root = new BinaryTreeNode(50);
        BinaryTreeNode node_1 = root.insertLeftNode(30);
        BinaryTreeNode node_2 = root.insertRightNode(80);
        BinaryTreeNode node_3 = node_1.insertLeftNode(20);
        BinaryTreeNode leaf_1 = node_1.insertRightNode(40);
        BinaryTreeNode leaf_2 = node_3.insertLeftNode(10);
        BinaryTreeNode node_4 = node_2.insertLeftNode(70);
        BinaryTreeNode leaf_3 = node_4.insertLeftNode(60);
        BinaryTreeNode node_5 = node_2.insertRightNode(90);
        BinaryTreeNode leaf_4 = node_5.insertLeftNode(85);
        BinaryTreeNode leaf_5 = node_5.insertRightNode(100);
        assertTrue(interviewCake.isBinarySearchTree(root));
    }

}
