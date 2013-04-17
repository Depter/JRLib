package org.jrlib.bootstrap.mcl.pseudodata;

import org.jrlib.TestData;
import org.jrlib.claimratio.ClaimRatio;
import org.jrlib.claimratio.SimpleClaimRatio;
import org.jrlib.claimratio.scale.ClaimRatioScale;
import org.jrlib.claimratio.scale.SimpleClaimRatioScale;
import org.jrlib.triangle.claim.ClaimTriangle;
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
