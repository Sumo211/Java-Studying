package com.leon.algorithm;

import java.util.Arrays;

/**
 * @see <a href="https://www.interviewcake.com/question/java/single-riffle-check">Source</a>
 * Great engineers have both the skill to see how to optimize their code and the wisdom to know when those optimizations aren't worth it
 */
// TODO: 2/28/2018 Write unit tests
public class SingleRiffleShuffle {

    // O(n^2) time and O(n^2) space
    boolean isSingleRiffle_V1(int[] half1, int[] half2, int[] shuffledDeck) {
        // base case
        if (shuffledDeck.length == 0) {
            return true;
        }

        // if the top of shuffledDeck is the same as the top of half1 (making sure first that we have a top card in half1)
        if (half1.length > 0 && half1[0] == shuffledDeck[0]) {

            // take the top cards off half1 and shuffledDeck and recurse
            return isSingleRiffle_V1(removeTopCard(half1), half2, removeTopCard(shuffledDeck));

        // if the top of shuffledDeck is the same as the top of half2
        } else if (half2.length > 0 && half2[0] == shuffledDeck[0]) {

            // take the top cards off half2 and shuffledDeck and recurse
            return isSingleRiffle_V1(half1, removeTopCard(half2), removeTopCard(shuffledDeck));

        // top of shuffledDeck doesn't match top of half1 or half2 so we know it's not a single riffle
        } else {
            return false;
        }
    }

    // each recursive step created its own new "slice" of the input array
    private int[] removeTopCard(int[] cards) {
        return Arrays.copyOfRange(cards, 1, cards.length);
    }

    // O(n) time and O(n) space
    boolean isSingleRiffle_V2(int[] half1, int[] half2, int[] shuffledDeck) {
        return isSingleRiffle(half1, half2, shuffledDeck, 0, 0, 0);
    }

    // be careful of the hidden time and space costs of array slicing! Consider tracking array indices "by hand" instead
    private boolean isSingleRiffle(int[] half1, int[] half2, int[] shuffledDeck,
                                   int shuffledDeckIndex, int half1Index, int half2Index) {
        // base case we've hit the end of shuffledDeck
        if (shuffledDeckIndex == shuffledDeck.length) {
            return true;
        }

        // if we still have cards in half1 and the "top" card in half1 is the same as the top card in shuffledDeck
        if (half1.length > 0 && half1[half1Index] == shuffledDeck[shuffledDeckIndex]) {

            half1Index++;

        // if we still have cards in half2 and the "top" card in half2 is the same as the top card in shuffledDeck
        } else if (half2.length > 0 && half2[half2Index] == shuffledDeck[shuffledDeckIndex]) {

            half2Index++;

        // if the top card in shuffledDeck doesn't match the top card in half1 or half2, this isn't a single riffle
        } else {
            return false;
        }

        // the current card in shuffledDeck has now been "accounted for" so move on to the next one
        shuffledDeckIndex++;
        return isSingleRiffle(half1, half2, shuffledDeck, shuffledDeckIndex, half1Index, half2Index);
    }

    // O(n) time and O(1) space
    boolean isSingleRiffle_V3(int[] half1, int[] half2, int[] shuffledDeck) {
        int half1Index = 0;
        int half2Index = 0;
        int half1MaxIndex = half1.length - 1;
        int half2MaxIndex = half2.length - 1;

        for (int card : shuffledDeck) {
            // if we still have cards in half1 and the "top" card in half1 is the same as the top card in shuffledDeck
            if (half1MaxIndex >= half1Index && half1[half1Index] == card) {

                half1Index++;

            // if we still have cards in half2 and the "top" card in half2 is the same as the top card in shuffledDeck
            } else if (half2MaxIndex >= half2Index && half2[half2Index] == card) {

                half2Index++;

            // if the top card in shuffledDeck doesn't match the top card in half1 or half2, this isn't a single riffle
            } else {
                return false;
            }
        }

        // the current card in shuffledDeck has now been "accounted for" so move on to the next one
        return true;
    }

}
