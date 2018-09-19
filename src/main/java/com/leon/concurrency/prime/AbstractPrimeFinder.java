package com.leon.concurrency.prime;

abstract class AbstractPrimeFinder {

    void timeAndCompute(final int number) {
        long start = System.nanoTime();

        int numberOfPrimes = countPrimes(number);

        long end = System.nanoTime();
        System.out.printf("Number of primes under %d is %d.\n", number, numberOfPrimes);
        System.out.println("Time (seconds) taken is " + (end - start) * 1.0e9);
    }

    abstract int countPrimes(final int number);

    int countPrimesInRange(final int lower, final int upper) {
        int total = 0;
        for (int i = lower; i <= upper; i++) {
            if (isPrime(i)) {
                total++;
            }
        }

        return total;
    }

    private boolean isPrime(final int number) {
        if (number <= 1) {
            return false;
        }

        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }

        return true;
    }

}
