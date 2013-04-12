package org.jrlib.claimratio;

import org.jrlib.TestConfig;
import org.jrlib.TestData;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.ratio.DefaultRatioTriangle;
import org.jrlib.triangle.ratio.RatioTriangle;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class DefaultCRMethodTest {
    
    private final static double[] EXPECTED = {
        1.23814637, 1.17121069, 1.13371303, 1.10002119, 
        1.09383745, 1.08878712, 1.08710960, 1.02798997
    };
    
    private RatioTriangle ratios;
    private DefaultCRMethod crm;
    
    public DefaultCRMethodTest() {
    }

    @Before
    public void setUp() {
        ClaimTriangle incurred = TestData.getCummulatedTriangle(TestData.INCURRED);
        ClaimTriangle paid = TestData.getCummulatedTriangle(TestData.PAID);
        ratios = new DefaultRatioTriangle(incurred, paid);
        crm = new DefaultCRMethod();
    }

    @Test
    public void testFit() {
        crm.fit(ratios);
        assertEquals(Double.NaN, crm.getValue(-1), TestConfig.EPSILON);
        int devs = ratios.getDevelopmentCount();
        for(int d=0; d<devs; d++)
            assertEquals(EXPECTED[d], crm.getValue(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, crm.getValue(devs), TestConfig.EPSILON);
    }
}