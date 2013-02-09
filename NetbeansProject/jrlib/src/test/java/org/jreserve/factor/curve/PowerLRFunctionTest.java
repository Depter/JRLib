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
public class PowerLRFunctionTest {
    //Excel solver
    private final static double PAID_R2 = 0.996267535480579;
    //Excel solver
    private final static double INCURRED_R2 = 0.990483739244736;
    
    private PowerLRFunction pf;

    public PowerLRFunctionTest() {
    }

    @Before
    public void setUp() {
        pf = new PowerLRFunction();
    }

    @Test
    public void testFit_Paid() {
        LinkRatio lr = FixedLinkRatio.getPaid();
        pf.fit(lr);
        double r2 = Regression.rSquareModel(lr, pf);
        assertTrue(PAID_R2 <= r2);
    }

    @Test
    public void testFit_Incurred() {
        LinkRatio lr = FixedLinkRatio.getIncurred();
        pf.fit(lr);
        double r2 = Regression.rSquareModel(lr, pf);
        assertTrue(INCURRED_R2 <= r2);
    }

    @Test
    public void testGetA() {
        assertEquals(1d, pf.getA(), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testGetB() {
        assertEquals(1d, pf.getB(), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testGetValue() {
        for(int d=0; d<7; d++)
            assertEquals(1d, pf.getValue(d+1), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testGradient() {
        double[] params = new double[] {0.3, 0.8};
        double x = 0.1;
        double[] expected = new double[] {1.00426305247512, -0.0453414526338525};
        assertArrayEquals(expected, pf.gradient(x, params), JRLibTestSuite.EPSILON);
        
        x = 0.5;
        expected = new double[] {1.0156563109007, -0.22927923316246};
        assertArrayEquals(expected, pf.gradient(x, params), JRLibTestSuite.EPSILON);
        
        x = 1.5;
        expected = new double[] {1.00779488982139, -0.682513672322028};
        assertArrayEquals(expected, pf.gradient(x, params), JRLibTestSuite.EPSILON);
        
        params = new double[] {0.7, 0.8};
        expected = new double[] {0.791950651676431, -0.370740502504225};
        assertArrayEquals(expected, pf.gradient(x, params), JRLibTestSuite.EPSILON);
        
        params = new double[] {0.7, 5d};
        expected = new double[] {0.296142272439538, -0.0221815709682548};
        assertArrayEquals(expected, pf.gradient(x, params), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testEquals() {
        PowerLRFunction pf1 = new PowerLRFunction();
        PowerLRFunction pf2 = null;
        assertFalse(pf1.equals(pf2));
        
        pf2 = new PowerLRFunction();
        assertTrue(pf1.equals(pf2));
        assertTrue(pf2.equals(pf1));
        
        pf1.fit(FixedLinkRatio.getPaid());
        pf2.fit(FixedLinkRatio.getIncurred());
        assertTrue(pf1.equals(pf2));
        assertTrue(pf2.equals(pf1));
    }
}