package com.leon.shinra;

public class SquareCalculator implements AreaCalculator {

    private double a;

    private Calculator calculator = new CalculatorImpl();

    SquareCalculator(double a) {
        this.a = a;
    }

    @Override
    public double calculate() {
        return calculator.multiply(a).power(2).get();
    }

}
