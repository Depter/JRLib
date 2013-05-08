package org.jreserve.jrlib.bootstrap.odp.scale;

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ConstantOdpResidualScaleTest {
    
    private final static double EXPECTED = 229.34986704;
    
    private OdpResidualScale scales;
    
    @Before
    public void setUp() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.TAYLOR_ASHE);
        LinkRatio lrs = new SimpleLinkRatio(cik);
        scales = new ConstantOdpResidualScale(lrs);
    }
    
    @Test
    public void testRecalculate() {
        for(int d=0; d<10; d++)
            assertEquals(EXPECTED, scales.getValue(d), TestConfig.EPSILON);
    }
}
