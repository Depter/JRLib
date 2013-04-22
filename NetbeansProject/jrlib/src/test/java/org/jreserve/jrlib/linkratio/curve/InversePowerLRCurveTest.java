package org.jreserve.jrlib.linkratio.curve;

import org.jreserve.jrlib.linkratio.curve.InversePowerLRCurve;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.DevelopmentFactors;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class InversePowerLRCurveTest {
    
    private final static double[] EXPCETD_PAID = {
        1.10309578, 1.02816278, 1.01318290, 1.00769326,
        1.00506620, 1.00360119, 1.00269844
   };
    
    private InversePowerLRCurve ip;
    
    public InversePowerLRCurveTest() {
    }

    @Before
    public void setUp() {
        ip = new InversePowerLRCurve();
    }

    @Test
    public void testFit_Paid() {
        LinkRatio lr = TestData.getLinkRatio(TestData.PAID);
        ip.fit(lr);
        assertEquals( 0.10309578, ip.getA(), TestConfig.EPSILON);
        assertEquals(-1.87212371, ip.getB(), TestConfig.EPSILON);
    }

    @Test
    public void testFit_Incurred() {
        LinkRatio lr = TestData.getLinkRatio(TestData.INCURRED);
        ip.fit(lr);
        assertEquals( 0.04717681, ip.getA(), TestConfig.EPSILON);
        assertEquals(-1.35805816, ip.getB(), TestConfig.EPSILON);
    }

    @Test
    public void testGetA() {
        assertEquals(1d, ip.getA(), TestConfig.EPSILON);
    }

    @Test
    public void testGetB() {
        assertEquals(1d, ip.getB(), TestConfig.EPSILON);
    }

    @Test
    public void testGetValue() {
        for(int d=1; d<10; d++)
            assertEquals(1d+(double)d, ip.getValue(d-1), TestConfig.EPSILON);
    
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.PAID);
        ip.fit(new SimpleLinkRatio(new DevelopmentFactors(cik)));
        for(int d=0; d<EXPCETD_PAID.length; d++)
            assertEquals(EXPCETD_PAID[d], ip.getValue(d), TestConfig.EPSILON);
    }

    @Test
    public void testEquals() {
        InversePowerLRCurve ip1 = new InversePowerLRCurve();
        InversePowerLRCurve ip2 = null;
        assertFalse(ip1.equals(ip2));
        
        ip2 = new InversePowerLRCurve();
        assertTrue(ip1.equals(ip2));
        assertTrue(ip2.equals(ip1));
        
        ip1.fit(TestData.getLinkRatio(TestData.PAID));
        ip2.fit(TestData.getLinkRatio(TestData.INCURRED));
        assertTrue(ip1.equals(ip2));
        assertTrue(ip2.equals(ip1));
    }
}
