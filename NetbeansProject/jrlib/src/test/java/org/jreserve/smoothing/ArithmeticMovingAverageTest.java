package org.jreserve.smoothing;

import static org.jreserve.JLibTestSuite.EPSILON;
import org.jreserve.triangle.Cell;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class ArithmeticMovingAverageTest {

    private final static double[] INPUT = {
        4873558, 5130849, 5945610, 6632221, 
        7020974, 8275452, 9000367, 9511539
    };
    
    private double[] input;
    private ArithmeticMovingAverage smoothing;
    
    public ArithmeticMovingAverageTest() {
    }
    
    @Before
    public void setUp() {
        int size = INPUT.length;
        input = new double[size];
        System.arraycopy(INPUT, 0, input, 0, size);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_MaLength() {
        new ArithmeticMovingAverage(new SmoothingCell[0], 0);
    }
    
    @Test
    public void testSmooth_Ma3() {
        smoothing = new ArithmeticMovingAverage(new SmoothingCell[0], 3);
        double[] expected = new double[] {
            4873558.000000000, 5130849.000000000, 5316672.3333333333, 5902893.3333333333, 
            6532935.000000000, 7309549.000000000, 8098931.000000000, 8929119.3333333333
        };
        smoothing.smooth(input);
        assertArrayEquals(expected, input, EPSILON);
    }
        
    @Test
    public void testSmooth_Ma5() {
        smoothing = new ArithmeticMovingAverage(new SmoothingCell[0], 5);
        double[] expected = new double[] {
            4873558.000, 5130849.000, 5945610.000, 6632221.000, 
            5920642.400, 6601021.200, 7374924.800, 8088110.600
        };
        smoothing.smooth(input);
        assertArrayEquals(expected, input, EPSILON);
    }

    @Test
    public void testSmooth_Ma9() {
        smoothing = new ArithmeticMovingAverage(new SmoothingCell[0], 9);
        smoothing.smooth(input);
        assertArrayEquals(INPUT, input, EPSILON);
    }
}