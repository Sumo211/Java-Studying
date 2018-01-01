package com.leon.shinra;

public class CircleCalculator implements AreaCalculator {

    private static final double PI = 3.1416;

    private double r;

    private Calculator calculator = new CalculatorImpl();

    CircleCalculator(double r) {
        this.r = r;
    }

    @Override
    public double calculate() {
        return calculator.multiply(r).power(2).multiply(PI).get();
    }

}
