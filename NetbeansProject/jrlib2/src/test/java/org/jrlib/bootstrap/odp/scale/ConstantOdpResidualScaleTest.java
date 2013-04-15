package org.jrlib.bootstrap.odp.scale;

import org.jrlib.TestConfig;
import org.jrlib.TestData;
import org.jrlib.bootstrap.odp.residuals.AdjustedOdpResidualTriangle;
import org.jrlib.bootstrap.odp.residuals.OdpResidualTriangle;
import org.jrlib.linkratio.LinkRatio;
import org.jrlib.linkratio.SimpleLinkRatio;
import org.jrlib.triangle.claim.ClaimTriangle;
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
        OdpResidualTriangle residuals = new AdjustedOdpResidualTriangle(lrs);
        scales = new ConstantOdpResidualScale(residuals);
    }
    
    @Test
    public void testRecalculate() {
        for(int d=0; d<10; d++)
            assertEquals(EXPECTED, scales.getValue(d), TestConfig.EPSILON);
    }
}
