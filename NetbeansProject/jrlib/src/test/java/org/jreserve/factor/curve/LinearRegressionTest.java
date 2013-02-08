package org.jreserve.factor.curve;

import org.jreserve.JRLibTestSuite;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Peter Decsi
 */
public class LinearRegressionTest {

    public LinearRegressionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testFit() {
        double[] y = {
            -1.398593622, -4.703530194, -4.091350214, -4.639882581,
            -5.660883376, -5.698202166, -6.259794345
        };
        double[] params = LinearRegression.fit(y);
        assertEquals(-2.04425103, params[0], JRLibTestSuite.EPSILON);
        assertEquals(-0.64794569, params[1], JRLibTestSuite.EPSILON);
    }
    
    @Test
    public void testPredict() {
        double[] found = LinearRegression.predict(-1d, 0.5, 10);
        assertEquals(10, found.length);
        for(int i=0; i<10; i++)
            assertEquals (-1d+(i+1)/2d, found[i], JRLibTestSuite.EPSILON);
    }
    
    @Test
    public void testRSquare() {
        double[] y = {
            -1.398593622, -4.703530194, -4.091350214, -4.639882581,
            -5.660883376, -5.698202166, -6.259794345
        };
        double[] params = LinearRegression.fit(y);
        double rs = LinearRegression.rSquareModel(y, params);
        assertEquals(0.753673789, rs, JRLibTestSuite.EPSILON);
    }

}