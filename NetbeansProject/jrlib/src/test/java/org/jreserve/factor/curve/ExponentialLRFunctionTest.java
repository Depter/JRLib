package org.jreserve.factor.curve;

import org.jreserve.JRLibTestSuite;
import org.jreserve.factor.LinkRatio;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author Peter Decsi
 */
public class ExponentialLRFunctionTest {
    //Excel solver
    private final static double PAID_R2 = 0.996222800585825;
    //Excel solver
    private final static double INCURRED_R2 = 0.990785271100597;
    
    private ExponentialLRFunction ef;
    
    public ExponentialLRFunctionTest() {
    }

    @Before
    public void setUp() {
        ef = new ExponentialLRFunction();
    }

    @Test
    public void testFit_Paid() {
        LinkRatio lr = FixedLinkRatio.getPaid();
        ef.fit(lr);
        double r2 = Regression.rSquareModel(lr, ef);
        assertTrue(PAID_R2 <= r2);
    }

    @Test
    public void testFit_Incurred() {
        LinkRatio lr = FixedLinkRatio.getIncurred();
        ef.fit(lr);
        double r2 = Regression.rSquareModel(lr, ef);
        assertTrue(INCURRED_R2 <= r2);
    }

    @Test
    public void testGetA() {
        assertEquals(1d, ef.getA(), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testGetB() {
        assertEquals(1d, ef.getB(), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testGetValue() {
        double[] expected = {
            1.36787944117144, 1.13533528323661, 1.04978706836786, 1.01831563888873, 
            1.00673794699909, 1.00247875217667, 1.00091188196555
        
        };
        for(int d=0; d<7; d++)
            assertEquals(expected[d], ef.getValue(d+1), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testGradient() {
        double[] params = new double[] {0.3, 0.8};
        double x = 0.1;
        double[] expected = new double[] {0.923116346386636, -0.0276934903915991};
        assertArrayEquals(expected, ef.gradient(x, params), JRLibTestSuite.EPSILON);
        
        x = 0.5;
        expected = new double[] {0.670320046035639, -0.100548006905346};
        assertArrayEquals(expected, ef.gradient(x, params), JRLibTestSuite.EPSILON);
        
        x = 1.5;
        expected = new double[] {0.301194211912202, -0.135537395360491};
        assertArrayEquals(expected, ef.gradient(x, params), JRLibTestSuite.EPSILON);
        
        params = new double[] {0.7, 0.8};
        expected = new double[] {0.301194211912202, -0.316253922507812};
        assertArrayEquals(expected, ef.gradient(x, params), JRLibTestSuite.EPSILON);
        
        params = new double[] {0.7, 5d};
        expected = new double[] {0.000553084370147834, -0.000580738588655225};
        assertArrayEquals(expected, ef.gradient(x, params), JRLibTestSuite.EPSILON);
    }


    @Test
    public void testEquals() {
        ExponentialLRFunction ef1 = new ExponentialLRFunction();
        ExponentialLRFunction ef2 = null;
        assertFalse(ef1.equals(ef2));
        
        ef2 = new ExponentialLRFunction();
        assertTrue(ef1.equals(ef2));
        assertTrue(ef2.equals(ef1));
        
        ef1.fit(FixedLinkRatio.getPaid());
        ef2.fit(FixedLinkRatio.getIncurred());
        assertTrue(ef1.equals(ef2));
        assertTrue(ef2.equals(ef1));
    }
}