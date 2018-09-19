package com.leon.concurrency.prime;

class SequentialPrimeFinder extends AbstractPrimeFinder {

    @Override
    protected int countPrimes(final int number) {
        return countPrimesInRange(1, number);
    }

    public static void main(String[] args) {
        SequentialPrimeFinder finder = new SequentialPrimeFinder();
        finder.timeAndCompute(10000001);
    }

}
