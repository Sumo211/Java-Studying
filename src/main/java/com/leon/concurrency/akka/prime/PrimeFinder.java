package com.leon.concurrency.akka.prime;

final class PrimeFinder {

    private PrimeFinder() {

    }

    static int countPrimesInRange(final int lower, final int upper) {
        int count = 0;
        for (int i = lower; i <= upper; i++) {
            if (isPrime(i)) {
                count++;
            }
        }

        return count;
    }

    private static boolean isPrime(final int input) {
        if (input <= 1) return false;

        final int limit = (int) Math.sqrt(input);
        for (int i = 2; i <= limit; i++) {
            if (input % i == 0) {
                return false;
            }
        }

        return true;
    }

}
