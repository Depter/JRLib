package org.jreserve.jrlib.bootstrap.mcl.pseudodata;

import org.jreserve.jrlib.bootstrap.mcl.pseudodata.MclAdjustedClaimRatioResiduals;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.claimratio.SimpleClaimRatio;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.SimpleClaimRatioScale;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclAdjustedClaimRatioResidualsTest {

    private MclAdjustedClaimRatioResiduals residuals;
    
    @Before
    public void setUp() {
        ClaimTriangle numerator = TestData.getCummulatedTriangle(TestData.INCURRED);
        ClaimTriangle denominator = TestData.getCummulatedTriangle(TestData.PAID);
        
        ClaimRatio crs = new SimpleClaimRatio(numerator, denominator);
        ClaimRatioScale scales = new SimpleClaimRatioScale(crs);
        residuals = new MclAdjustedClaimRatioResiduals(scales);
    }
    
    @Test
    public void testCalculateP() {
        assertEquals(13, residuals.recalculateP());
    }
    
    @Test
    public void testCalculateN() {
        assertEquals(28, residuals.recalculateN());
    }
}
