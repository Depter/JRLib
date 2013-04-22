package org.jreserve.jrlib.triangle.smoothing;

import org.jreserve.jrlib.triangle.smoothing.GeometricMovingAverage;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;
import static org.jreserve.jrlib.TestConfig.EPSILON;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class GeometricMovingAverageTest {

    private final static double[] INPUT = {
        4873558, 5130849, 5945610, 6632221, 
        7020974, 8275452, 9000367, 9511539
    };
    
    private double[] input;
    private GeometricMovingAverage smoothing;

    public GeometricMovingAverageTest() {
    }

    @Before
    public void setUp() {
        int size = INPUT.length;
        input = new double[size];
        System.arraycopy(INPUT, 0, input, 0, size);
    }
    
    @Test
    public void testSmooth_Ma3() {
        smoothing = new GeometricMovingAverage(new SmoothingCell[0], 3);
        double[] expected = new double[] {
            4873558.00000000, 5130849.00000000, 5297576.82670085, 5870587.25187823, 
            6517548.28843699, 7276949.47218994, 8056562.28817919, 8914580.51627531
        };
        smoothing.smooth(input);
        assertArrayEquals(expected, input, EPSILON);
    }
    
    @Test
    public void testSmooth_Ma5() {
        smoothing = new GeometricMovingAverage(new SmoothingCell[0], 5);
        double[] expected = new double[] {
            4873558.00000000, 5130849.00000000, 5945610.00000000, 6632221.00000000, 
            5862159.47725441, 6516985.36995764, 7292239.82302271, 8010724.90501880
        };
        smoothing.smooth(input);
        assertArrayEquals(expected, input, EPSILON);
    }

}
