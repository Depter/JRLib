package org.jreserve.util;

import static org.jreserve.JRLibTestSuite.EPSILON;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class MathUtilTest {

    public MathUtilTest() {
    }
    
    @Test
    public void testMean() {
        double[] x = {1d, Double.NaN, 2d, 3d, Double.NaN, 4d, 5d, 6d};
        assertEquals(3.5, MathUtil.mean(x), EPSILON);
    }
    
    @Test
    public void testMean_Empty() {
        double[] x = new double[0];
        assertEquals(Double.NaN, MathUtil.mean(x), EPSILON);
    }
    
    @Test
    public void testSigma() {
        double[] x = {1d, Double.NaN, 2d, 3d, Double.NaN, 4d, 5d, 6d};
        double expected = 1.87082869;
        assertEquals(expected, MathUtil.standardDeviation(x), EPSILON);
    }
}