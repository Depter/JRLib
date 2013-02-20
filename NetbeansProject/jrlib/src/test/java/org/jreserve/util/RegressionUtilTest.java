package org.jreserve.util;

import static org.jreserve.JRLibTestSuite.EPSILON;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class RegressionUtilTest {

    public RegressionUtilTest() {
    }
    
    @Test
    public void testLinearRegression() {
        double[] y = {
            -1.398593622, -4.703530194, -4.091350214, -4.639882581,
            -5.660883376, -5.698202166, -6.259794345
        };
        double[] params = RegressionUtil.linearRegression(y);
        assertEquals(-2.04425103, params[0], EPSILON);
        assertEquals(-0.64794569, params[1], EPSILON);
    }
    
    @Test
    public void testPredict() {
        double[] found = RegressionUtil.predict(-1d, 0.5, 10);
        assertEquals(10, found.length);
        for(int i=0; i<10; i++)
            assertEquals (-1d+(i+1)/2d, found[i], EPSILON);
    }
    
    @Test
    public void testRSquare() {
        double[] y = {
            -1.398593622, -4.703530194, -4.091350214, -4.639882581,
            -5.660883376, -5.698202166, -6.259794345
        };
        double[] params = RegressionUtil.linearRegression(y);
        double rs = RegressionUtil.rSquareModel(y, params);
        assertEquals(0.753673789, rs, EPSILON);
    }
}