package org.jreserve.factor.curve;

import org.jreserve.JRLibTestSuite;
import org.jreserve.factor.LinkRatio;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class InversePowerLRFunctionTest {
    //Excel Solver
    private final static double PAID_R2 = 0.990851661370006;
    //Excel Solver
    private final static double INCURRED_R2 = 0.990851661370006;
    
    private InversePowerLRFunction ip;
    
    public InversePowerLRFunctionTest() {
    }

    @Before
    public void setUp() {
        ip = new InversePowerLRFunction();
    }

    @Test
    public void testFit_Paid() {
        LinkRatio lr = FixedLinkRatio.getPaid();
        ip.fit(lr);
        assertTrue(PAID_R2 <= Regression.rSquareModel(lr, ip));
    }

    @Test
    public void testFit_Incurred() {
        LinkRatio lr = FixedLinkRatio.getIncurred();
        ip.fit(lr);
        assertTrue(INCURRED_R2 <= Regression.rSquareModel(lr, ip));
    }

    @Test
    public void testGetA() {
        assertEquals(1d, ip.getA(), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testGetB() {
        assertEquals(1d, ip.getB(), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testGetValue() {
        for(int d=1; d<10; d++)
            assertEquals(1d+(double)d, ip.getValue(d), JRLibTestSuite.EPSILON);
    }
    
    @Test
    public void testGetGradient() {
        double[] params = new double[] {0.3, 0.8};
        double x = 0.1;
        double[] expected = new double[] {0.158489319246111, -0.109480543168461};
        assertArrayEquals(expected, ip.gradient(x, params), JRLibTestSuite.EPSILON);
        
        x = 0.5;
        expected = new double[] {0.574349177498518, -0.119432553912006};
        assertArrayEquals(expected, ip.gradient(x, params), JRLibTestSuite.EPSILON);
        
        x = 1.5;
        expected = new double[] {1.38316186722259, 0.16824716280735};
        assertArrayEquals(expected, ip.gradient(x, params), JRLibTestSuite.EPSILON);
        
        params = new double[] {0.7, 0.8};
        expected = new double[] {1.38316186722259, 0.392576713217149};
        assertArrayEquals(expected, ip.gradient(x, params), JRLibTestSuite.EPSILON);
        
        params = new double[] {0.7, 5d};
        expected = new double[] {7.59375, 2.15530046528746};
        assertArrayEquals(expected, ip.gradient(x, params), JRLibTestSuite.EPSILON);
    }
}