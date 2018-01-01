package com.leon.shinra;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AreaCalculatorTest {

    private static final double DELTA = 0.00001;

    private static final double PI = 3.1416;

    private List<AreaCalculator> areaCalculators;

    @Before
    public void init() {
        areaCalculators = new ArrayList<>();
        areaCalculators.add(new CircleCalculator(4));
        areaCalculators.add(new SquareCalculator(4));
        areaCalculators.add(new TriangleCalculator(4, 2));
    }

    @Test
    public void testCalculate_OK() {
        List<Double> output = new ArrayList<>();
        areaCalculators.forEach(element -> output.add(element.calculate()));
        assertEquals(PI * 4 * 4, output.get(0), DELTA);
        assertEquals(4 * 4, output.get(1), DELTA);
        assertEquals((4 * 2) / 2, output.get(2), DELTA);
    }

}
