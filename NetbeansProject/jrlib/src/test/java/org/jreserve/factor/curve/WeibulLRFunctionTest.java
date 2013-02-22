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
public class WeibulLRFunctionTest {
    
    private WeibulLRFunction wf;

    public WeibulLRFunctionTest() {
    }

    @Before
    public void setUp() {
        wf = new WeibulLRFunction();
    }

    @Test
    public void testFit_Paid() {
        LinkRatio lr = FixedLinkRatio.getPaid();
        wf.fit(lr);
        assertEquals(2.16807672, wf.getA(), JRLibTestSuite.EPSILON);
        assertEquals(1.19042212, wf.getB(), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testFit_Incurred() {
        LinkRatio lr = FixedLinkRatio.getIncurred();
        wf.fit(lr);
        assertEquals(1.36002190, wf.getA(), JRLibTestSuite.EPSILON);
        assertEquals(1.34407415, wf.getB(), JRLibTestSuite.EPSILON);

    }

    @Test
    public void testGetA() {
        assertEquals(1d, wf.getA(), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testGetB() {
        assertEquals(1d, wf.getB(), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testGetValue() {
        for(int d=0; d<7; d++)
            assertEquals(1.581976707, wf.getValue(d+1), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testEquals() {
        WeibulLRFunction wf1 = new WeibulLRFunction();
        WeibulLRFunction wf2 = null;
        assertFalse(wf1.equals(wf2));
        
        wf2 = new WeibulLRFunction();
        assertTrue(wf1.equals(wf2));
        assertTrue(wf2.equals(wf1));
        
        wf1.fit(FixedLinkRatio.getPaid());
        wf2.fit(FixedLinkRatio.getIncurred());
        assertTrue(wf1.equals(wf2));
        assertTrue(wf2.equals(wf1));
    }
}