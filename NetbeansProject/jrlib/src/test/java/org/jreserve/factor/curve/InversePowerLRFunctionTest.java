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
        assertEquals(0.15379669, ip.getA(), JRLibTestSuite.EPSILON);
        assertEquals(2.27209663, ip.getB(), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testFit_Incurred() {
        LinkRatio lr = FixedLinkRatio.getIncurred();
        ip.fit(lr);
        assertEquals(0.257159887, ip.getA(), JRLibTestSuite.EPSILON);
        assertEquals(3.053855492, ip.getB(), JRLibTestSuite.EPSILON);
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
    public void testEquals() {
        InversePowerLRFunction ip1 = new InversePowerLRFunction();
        InversePowerLRFunction ip2 = null;
        assertFalse(ip1.equals(ip2));
        
        ip2 = new InversePowerLRFunction();
        assertTrue(ip1.equals(ip2));
        assertTrue(ip2.equals(ip1));
        
        ip1.fit(FixedLinkRatio.getPaid());
        ip2.fit(FixedLinkRatio.getIncurred());
        assertTrue(ip1.equals(ip2));
        assertTrue(ip2.equals(ip1));
    }
}