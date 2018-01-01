package com.leon.shinra;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import java.util.stream.IntStream;

public class CalculatorImpl implements Calculator {

    private double initialValue;

    private Queue<String> operations;

    private Queue<Double> inputs;

    CalculatorImpl() {
        this.operations = new ArrayDeque<>();
        this.inputs = new ArrayDeque<>();
    }

    @Override
    public Calculator plus(double input) {
        this.operations.add("+");
        this.inputs.add(input);
        return this;
    }

    @Override
    public Calculator minus(double input) {
        this.operations.add("-");
        this.inputs.add(input);
        return this;
    }

    @Override
    public Calculator multiply(double input) {
        this.operations.add("*");
        this.inputs.add(input);
        return this;
    }

    @Override
    public Calculator divide(double input) {
        this.operations.add("/");
        this.inputs.add(input);
        return this;
    }

    @Override
    public Calculator power(double exp) {
        this.operations.add("^");
        this.inputs.add(exp);
        return this;
    }

    @Override
    public double get() {
        String firstOp = operations.peek();
        this.initialValue = ("*".equals(firstOp) || "/".equals(firstOp) || "^".equals(firstOp)) ? 1 : 0;

        Iterator<String> i1 = operations.iterator();
        Iterator<Double> i2 = inputs.iterator();
        while (i1.hasNext()) {
            calculate(i1.next(), i2.next());
        }

        return this.initialValue;
    }

    private void calculate(String operator, double value) {
        switch (operator) {
            case "+":
                this.initialValue += value;
                break;
            case "-":
                this.initialValue -= value;
                break;
            case "*":
                this.initialValue *= value;
                break;
            case "/":
                if (value == 0) throw new ArithmeticException("Cannot divide by 0");
                this.initialValue /= value;
                break;
            case "^":
                if (value == 0) {
                    this.initialValue = 1;
                } else if (value > 0) {
                    IntStream.range(1, (int) value).forEach(index -> this.initialValue *= this.initialValue);
                } else {
                    throw new IllegalArgumentException("Invalid argument");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid operator");
        }
    }

}
