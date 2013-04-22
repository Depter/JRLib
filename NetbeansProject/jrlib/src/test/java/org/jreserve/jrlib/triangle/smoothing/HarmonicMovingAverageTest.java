package org.jreserve.jrlib.triangle.smoothing;

import org.jreserve.jrlib.triangle.smoothing.HarmonicMovingAverage;
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
public class HarmonicMovingAverageTest {

    private final static double[] INPUT = {
        4873558, 5130849, 5945610, 6632221, 
        7020974, 8275452, 9000367, 9511539
    };
    
    private double[] input;
    private HarmonicMovingAverage smoothing;

    public HarmonicMovingAverageTest() {
    }

    @Before
    public void setUp() {
        int size = INPUT.length;
        input = new double[size];
        System.arraycopy(INPUT, 0, input, 0, size);
    }
    
    @Test
    public void testSmooth_Ma3() {
        smoothing = new HarmonicMovingAverage(new SmoothingCell[0], 3);
        double[] expected = new double[] {
            4873558.00000000, 5130849.00000000, 5279090.20265317, 5838053.64907144, 
            6501939.95319046, 7245523.54772155, 8013328.14998032, 8899928.53423601
        };
        smoothing.smooth(input);
        assertArrayEquals(expected, input, EPSILON);
    }
    
    @Test
    public void testSmooth_Ma5() {
        smoothing = new HarmonicMovingAverage(new SmoothingCell[0], 5);
        double[] expected = new double[] {
            4873558.00000000, 5130849.00000000, 5945610.00000000, 6632221.00000000, 
            5803903.47601912, 6433850.54463213, 7211399.89342641, 7932846.33745969
        };
        smoothing.smooth(input);
        assertArrayEquals(expected, input, EPSILON);
    }

}
