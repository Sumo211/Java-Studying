package com.leon.shinra;

public class TriangleCalculator implements AreaCalculator {

    private double h;

    private double d;

    private Calculator calculator = new CalculatorImpl();

    TriangleCalculator(double h, double d) {
        this.h = h;
        this.d = d;
    }

    @Override
    public double calculate() {
        return calculator.multiply(h).multiply(d).divide(2).get();
    }

}
