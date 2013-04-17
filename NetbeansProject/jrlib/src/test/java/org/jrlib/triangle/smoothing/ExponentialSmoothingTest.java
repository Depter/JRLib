package org.jrlib.triangle.smoothing;

import static org.jrlib.TestConfig.EPSILON;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExponentialSmoothingTest {

    private final static double[] INPUT = {
        4873558, 5130849, 5945610, 6632221, 
        7020974, 8275452, 9000367, 9511539
    };
    
    private double[] input;
    private ExponentialSmoothing smoothing;

    public ExponentialSmoothingTest() {
    }

    @Before
    public void setUp() {
        int size = INPUT.length;
        input = new double[size];
        System.arraycopy(INPUT, 0, input, 0, size);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_AlphaLess0() {
        new ExponentialSmoothing(new SmoothingCell[0], -EPSILON);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_AlphaMore1() {
        new ExponentialSmoothing(new SmoothingCell[0], 1+EPSILON);
    }
    
    @Test
    public void testSmooth_Alpha0() {
        smoothing = new ExponentialSmoothing(new SmoothingCell[0], 0d);
        smoothing.smooth(input);
        for(int i=0; i<input.length; i++)
            assertEquals(INPUT[0], input[i], EPSILON);
    }
    
    @Test
    public void testSmooth_Alpha1() {
        smoothing = new ExponentialSmoothing(new SmoothingCell[0], 1d);
        smoothing.smooth(input);
        for(int i=0; i<input.length; i++)
            assertEquals(INPUT[i], input[i], EPSILON);
    }
    
    @Test
    public void testSmooth_Alpha0_2() {
        smoothing = new ExponentialSmoothing(new SmoothingCell[0], 0.2);
        double[] expected = new double[] {
            4873558.00000000, 4925016.20000000, 5129134.96000000, 5429752.16800000, 
            5747996.53440000, 6253487.62752000, 6802863.50201600, 7344598.60161280
        };
        smoothing.smooth(input);
        assertArrayEquals(expected, input, EPSILON);
    }
    
    @Test
    public void testSmooth_Alpha0_8() {
        smoothing = new ExponentialSmoothing(new SmoothingCell[0], 0.8);
        double[] expected = new double[] {
            4873558.00000000, 5079390.80000000, 5772366.16000000, 6460250.03200000, 
            6908829.20640000, 8002127.44128000, 8800719.08825600, 9369375.01765120
        };
        smoothing.smooth(input);
        assertArrayEquals(expected, input, EPSILON);
    }

}
