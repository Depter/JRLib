package org.jrlib.util;

import org.jrlib.TestConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RegressionUtilTest {
    
    private final static double INTERCEPT = 0.5;
    private final static double SLOPE = 0.25;
    
    private final static double[] X = {
        1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d
    };
    
    
    private static double[] createObserved(double[] x, double intercept, double slope) {
        double[] y = new double[x.length];
        for(int i=0; i<x.length; i++)
            y[i] = intercept + slope * x[i];
        return y;
    }
    
    public RegressionUtilTest() {
    }
    
    @Test
    public void testLinearRegression() {
        double[] y = createObserved(X, INTERCEPT, SLOPE);
        double[] params = RegressionUtil.linearRegression(y);
        assertEquals(INTERCEPT, params[0], TestConfig.EPSILON);
        assertEquals(SLOPE, params[1], TestConfig.EPSILON);
        
        params = RegressionUtil.linearRegression(y, false);
        assertEquals(0d, params[0], TestConfig.EPSILON);
        assertEquals(0.33823529, params[1], TestConfig.EPSILON);
       
        double[] x = {0d, 1d, 2d, 3d, 4d, 5d, 6d, 7d};
        params = RegressionUtil.linearRegression(x, y);
        assertEquals(0.75, params[0], TestConfig.EPSILON);
        assertEquals(SLOPE, params[1], TestConfig.EPSILON);
    }
    
    @Test
    public void testPredict() {
        double[] found = RegressionUtil.predict(-1d, 0.5, 10);
        assertEquals(10, found.length);
        for(int i=0; i<10; i++)
            assertEquals (-1d+(i+1)/2d, found[i], TestConfig.EPSILON);
    }
    
    @Test
    public void testRSquare() {
        double[] y = {
            -1.398593622, -4.703530194, -4.091350214, -4.639882581,
            -5.660883376, -5.698202166, -6.259794345
        };
        double[] params = RegressionUtil.linearRegression(y);
        double rs = RegressionUtil.rSquareModel(y, params);
        assertEquals(0.753673789, rs, TestConfig.EPSILON);
    }

}
