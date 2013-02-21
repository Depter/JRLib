package org.jreserve.factor.curve;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.DevelopmentFactors;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.linkratio.SimpleLinkRatio;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.TriangleFactory;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class InversePowerLRFunctionTest {
    
    private final static double[] EXPCETD_PAID = {
        1.10309578, 1.02816278, 1.01318290, 1.00769326,
        1.00506620, 1.00360119, 1.00269844
   };
    
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
        assertEquals( 0.10309578, ip.getA(), JRLibTestSuite.EPSILON);
        assertEquals(-1.87212371, ip.getB(), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testFit_Incurred() {
        LinkRatio lr = FixedLinkRatio.getIncurred();
        ip.fit(lr);
        assertEquals( 0.04717668, ip.getA(), JRLibTestSuite.EPSILON);
        assertEquals(-1.35805726, ip.getB(), JRLibTestSuite.EPSILON);
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
    
        Triangle cik = TriangleFactory.create(TestData.PAID).cummulate().build();
        ip.fit(new SimpleLinkRatio(new DevelopmentFactors(cik)));
        for(int d=0; d<EXPCETD_PAID.length; d++)
            assertEquals(EXPCETD_PAID[d], ip.getValue(d+1), JRLibTestSuite.EPSILON);
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