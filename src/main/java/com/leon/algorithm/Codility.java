package com.leon.algorithm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Codility {

    // TODO: 12/28/2017 Write unit test for large input (performance test)
    public int findMissingIntegerNotOptimized(int[] input) {
        List<Integer> temp = Arrays.stream(input).boxed().sorted().collect(Collectors.toList());
        for (int i = 1; i <= 1000000; i++) {
            if (!temp.contains(i)) {
                return i;
            }
        }

        return 1000000;
    }

    // TODO: 12/28/2017 Write unit test for edge case (input has only one element)
    public int findMissingIntegerOptimized(int[] input) {
        int size = input.length;
        int shift = segregate(input, size);
        int[] arr2 = new int[size - shift];
        int j = 0;

        for (int i = shift; i < size; i++) {
            arr2[j] = input[i];
            j++;
        }

        return findMissingPositive(arr2, j);
    }

    private int findMissingPositive(int[] arr, int size) {
        int i;
        for (i = 0; i < size; i++) {
            if (Math.abs(arr[i]) - 1 < size && arr[Math.abs(arr[i]) - 1] > 0) {
                arr[Math.abs(arr[i]) - 1] = -arr[Math.abs(arr[i]) - 1];
            }
        }

        for (i = 0; i < size; i++) {
            if (arr[i] > 0) {
                return i + 1;
            }
        }

        return size + 1;
    }

    private int segregate(int[] arr, int size) {
        int j = 0;

        int temp;
        for (int i = 0; i < size; i++) {
            if (arr[i] < 0) {
                temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                j++;
            }
        }

        return j;
    }

    // TODO: 1/1/2018 Write unit tests
    public int findMinLengthOfArrayOfBinarian(int[] A) {
        int binarian = calculateBinarian(A);
        int sqrtOfBinarian = (int) Math.sqrt(binarian);

        int[] temp = createTempArray(sqrtOfBinarian);
        Set<Set<Integer>> allSubSets = powerSet(temp);

        int minLength = temp.length;
        for (Set<Integer> subset : allSubSets) {
            int sum = 0;
            for (Integer element : subset) {
                sum += Math.pow(2, element);
            }

            if (sum == binarian) {
                if (subset.size() < minLength) {
                    minLength = subset.size();
                }
            }
        }

        return minLength;
    }

    private int calculateBinarian(int[] input) {
        int sum = 0;
        for (int element : input) {
            sum += Math.pow(2, element);
        }

        return sum;
    }

    private int[] createTempArray(int length) {
        int[] temp = new int[length + 1];
        for (int i = 0; i <= length; i++) {
            temp[i] = i;
        }

        return temp;
    }

    private Set<Set<Integer>> powerSet(int[] input) {
        final int set_length = 1 << input.length;
        Set<Set<Integer>> result = new HashSet<>();
        for (int binarySet = 0; binarySet < set_length; binarySet++) {
            Set<Integer> subset = new HashSet<>();
            for (int bit = 0; bit < input.length; bit++) {
                int mask = 1 << bit;
                if ((binarySet & mask) != 0) {
                    subset.add(input[bit]);
                }
            }
            result.add(subset);
        }

        return result;
    }

}
