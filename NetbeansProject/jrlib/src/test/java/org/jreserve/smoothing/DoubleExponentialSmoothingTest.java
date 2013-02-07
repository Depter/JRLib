package org.jreserve.smoothing;

import static org.jreserve.JLibTestSuite.EPSILON;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class DoubleExponentialSmoothingTest {

    private final static double[] INPUT = {
        4873558, 5130849, 5945610, 6632221, 
        7020974, 8275452, 9000367, 9511539
    };
    
    private double[] input;
    private DoubleExponentialSmoothing smoothing;

    public DoubleExponentialSmoothingTest() {
    }

    @Before
    public void setUp() {
        int size = INPUT.length;
        input = new double[size];
        System.arraycopy(INPUT, 0, input, 0, size);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_AlphaLess0() {
        new DoubleExponentialSmoothing(new SmoothingCell[0], -EPSILON, 0.5);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_AlphaMore1() {
        new DoubleExponentialSmoothing(new SmoothingCell[0], 1+EPSILON, 0.5);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_BetaLess0() {
        new DoubleExponentialSmoothing(new SmoothingCell[0], 0.5, -EPSILON);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_BetaMore1() {
        new DoubleExponentialSmoothing(new SmoothingCell[0], 0.5, 1+EPSILON);
    }

    @Test
    public void testSmooth_0_0() {
        smoothing = new DoubleExponentialSmoothing(new SmoothingCell[0], 0d, 0d);
        double[] expected = new double[] {
            4873558, 5130849, 5388140, 5645431, 
            5902722, 6160013, 6417304, 6674595
        };
        smoothing.smooth(input);
        assertArrayEquals(expected, input, EPSILON);
    }

    @Test
    public void testSmooth_1_1() {
        smoothing = new DoubleExponentialSmoothing(new SmoothingCell[0], 1d, 1d);
        smoothing.smooth(input);
        for(int i=0; i<INPUT.length; i++)
            assertEquals(INPUT[i], input[i], EPSILON);
    }

    @Test
    public void testSmooth_8_2() {
        smoothing = new DoubleExponentialSmoothing(new SmoothingCell[0], 0.8, 0.2);
        double[] expected = new double[] {
            4873558.00000000, 5130849.00000000, 5834116.00000000, 6541897.24000000, 
            7008907.68960000, 8107822.78918400, 8934358.48283136, 9519164.58430781
        };
        smoothing.smooth(input);
        assertArrayEquals(expected, input, EPSILON);
    }

    @Test
    public void testSmooth_2_8() {
        smoothing = new DoubleExponentialSmoothing(new SmoothingCell[0], 0.2, 0.8);
        double[] expected = new double[] {
            4873558.00000000, 5130849.00000000, 5499634.00000000, 6003340.36000000, 
            6584676.95040000, 7370449.35065600, 8288850.69475584, 9239648.77887478
        };
        smoothing.smooth(input);
        assertArrayEquals(expected, input, EPSILON);
    }

}