package org.jreserve.jrlib.claimratio;

import org.jreserve.jrlib.claimratio.ClaimRatioCalculator;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.DefaultRatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class ClaimRatioCalculatorTest {
    
    private final static double[] EXPECTED = {
        1.23814637, 1.17121069, 1.13371303, 1.10002119, 
        1.09383745, 1.08878712, 1.08710960, 1.02798997
    };
    
    private RatioTriangle ratios;
    private ClaimRatioCalculator crm;
    
    public ClaimRatioCalculatorTest() {
    }

    @Before
    public void setUp() {
        ClaimTriangle incurred = TestData.getCummulatedTriangle(TestData.INCURRED);
        ClaimTriangle paid = TestData.getCummulatedTriangle(TestData.PAID);
        ratios = new DefaultRatioTriangle(incurred, paid);
        crm = new ClaimRatioCalculator(ratios);
    }

    @Test
    public void testRecalculate() {
        int devs = ratios.getDevelopmentCount();
        assertEquals(devs, crm.getLength());
        
        assertEquals(Double.NaN, crm.getValue(-1), TestConfig.EPSILON);
        for(int d=0; d<devs; d++)
            assertEquals(EXPECTED[d], crm.getValue(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, crm.getValue(devs), TestConfig.EPSILON);
    }
}