package com.leon.shinra;

public interface Calculator {

    Calculator plus(double input);

    Calculator minus(double input);

    Calculator multiply(double input);

    Calculator divide(double input);

    Calculator power(double exp);

    double get();

}
