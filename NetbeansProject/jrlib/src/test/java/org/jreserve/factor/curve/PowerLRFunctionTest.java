package org.jreserve.factor.curve;

import org.jreserve.JRLibTestSuite;
import org.jreserve.factor.linkratio.FixedLinkRatio;
import org.jreserve.factor.linkratio.LinkRatio;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class PowerLRFunctionTest {
    
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
        assertEquals(1.13911782, pf.getA(), JRLibTestSuite.EPSILON);
        assertEquals(0.51942242, pf.getB(), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testFit_Incurred() {
        LinkRatio lr = FixedLinkRatio.getIncurred();
        pf.fit(lr);
        assertEquals(2.15562992, pf.getA(), JRLibTestSuite.EPSILON);
        assertEquals(0.30319318, pf.getB(), JRLibTestSuite.EPSILON);
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