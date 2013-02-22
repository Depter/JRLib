package org.jreserve.factor.curve;

import org.jreserve.factor.linkratio.FixedLinkRatio;
import org.jreserve.JRLibTestSuite;
import org.jreserve.factor.linkratio.LinkRatio;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExponentialLRFunctionTest {
    
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
        assertEquals(0.139573203, ef.getA(), JRLibTestSuite.EPSILON);
        assertEquals(0.667524784, ef.getB(), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testFit_Incurred() {
        LinkRatio lr = FixedLinkRatio.getIncurred();
        ef.fit(lr);
        assertEquals(0.849803581, ef.getA(), JRLibTestSuite.EPSILON);
        assertEquals(1.212596053, ef.getB(), JRLibTestSuite.EPSILON);
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
            assertEquals(expected[d], ef.getValue(d), JRLibTestSuite.EPSILON);
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