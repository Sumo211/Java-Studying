package com.leon.algorithm;

import java.util.BitSet;

public class HackerRank {

    public static void main(String[] args) {
        /*int x = 1, y = 1, z = 0;
        if (!(x++ > 1 && y++ > 1)) {
            z = x + y;
        }

        System.out.println(z);*/

        /*int a = 5, b = 10;
        int signal = ((a - b) >> 31) & 0x1;
        int result = a - (signal * (a - b));
        System.out.println(String.format("The higher number of %d and %d is %d", a, b, result));*/
    }

    private static int getMissingNumber(int[] numbers, int totalCount) {
        int expectedSum = Math.round(totalCount * ((totalCount + 1) / 2.0f));

        int actualSum = 0;
        for (int i : numbers) {
            actualSum += i;
        }

        return expectedSum - actualSum;
    }

    private static int[] getMissingNumbers(int[] numbers, int totalCount) {
        int missingCount = totalCount - numbers.length;
        int[] missingNumbers = new int[missingCount];

        BitSet bitSet = new BitSet(totalCount);
        for (int number : numbers) {
            bitSet.set(number - 1);
        }

        int lastMissingIndex = 0;
        for (int i = 0; i < missingCount; i++) {
            lastMissingIndex = bitSet.nextClearBit(lastMissingIndex);
            missingNumbers[i] = ++lastMissingIndex;
        }

        return missingNumbers;
    }

}
