package org.jrlib.estimate.mcl;

import org.jrlib.TestConfig;
import org.jrlib.TestData;
import org.jrlib.claimratio.ClaimRatio;
import org.jrlib.claimratio.SimpleClaimRatio;
import org.jrlib.claimratio.scale.ClaimRatioScale;
import org.jrlib.claimratio.scale.SimpleClaimRatioScale;
import org.jrlib.claimratio.scale.residuals.CRResidualTriangle;
import org.jrlib.claimratio.scale.residuals.ClaimRatioResidualTriangleCorrection;
import org.jrlib.claimratio.scale.residuals.ClaimRatioResiduals;
import org.jrlib.linkratio.LinkRatio;
import org.jrlib.linkratio.curve.DefaultLinkRatioSmoothing;
import org.jrlib.linkratio.curve.LinkRatioSmoothingSelection;
import org.jrlib.linkratio.curve.UserInputLRCurve;
import org.jrlib.linkratio.scale.LinkRatioScale;
import org.jrlib.linkratio.scale.SimpleLinkRatioScale;
import org.jrlib.linkratio.scale.residuals.LRResidualTriangle;
import org.jrlib.linkratio.scale.residuals.LinkRatioResiduals;
import org.jrlib.triangle.claim.ClaimTriangle;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclCorrelationTest {
    
    private final static double PAID_LAMBDA = 0.15210439;
    private final static double INCURRED_LAMBDA = 0.40562407;
    
    @Test
    public void testGetCorrelation_Paid() {
        ClaimTriangle paid = TestData.getCummulatedTriangle(TestData.PAID);
        ClaimTriangle incurred = TestData.getCummulatedTriangle(TestData.INCURRED);
        
        LRResidualTriangle lrResiduals = createLrResiduals(paid, 1.05, 1.02);
        CRResidualTriangle crResiduals = createCrResiduals(incurred, paid);
        
        MclCorrelation lambda = new MclCorrelation(lrResiduals, crResiduals);
        assertEquals(PAID_LAMBDA, lambda.getValue(), TestConfig.EPSILON);
    }
    
    private LRResidualTriangle createLrResiduals(ClaimTriangle cik, double t1, double t2) {
        LinkRatio lr = createLinkRatio(cik, t1, t2);
        LinkRatioScale scale = new SimpleLinkRatioScale(lr);
        return new LinkRatioResiduals(scale);
    }
    
    private LinkRatio createLinkRatio(ClaimTriangle cik, double t1, double t2) {
        LinkRatioSmoothingSelection lrs = new DefaultLinkRatioSmoothing(cik);
        lrs.setDevelopmentCount(9);
        UserInputLRCurve tail = new UserInputLRCurve();
        tail.setValue(7, t1);
        tail.setValue(8, t2);
        lrs.setMethod(tail, 7, 8);
        return lrs;
    }
    
    private CRResidualTriangle createCrResiduals(ClaimTriangle numerator, ClaimTriangle denumerator) {
        ClaimRatio crs = new SimpleClaimRatio(numerator, denumerator);
        ClaimRatioScale scales = new SimpleClaimRatioScale(crs);
        CRResidualTriangle residuals = new ClaimRatioResiduals(scales);
        return new ClaimRatioResidualTriangleCorrection(residuals, 0, 6, Double.NaN);
    }
    
    @Test
    public void testGetCorrelation_Incurred() {
        ClaimTriangle paid = TestData.getCummulatedTriangle(TestData.PAID);
        ClaimTriangle incurred = TestData.getCummulatedTriangle(TestData.INCURRED);
        
        LRResidualTriangle lrResiduals = createLrResiduals(incurred, 1.03, 1.01);
        CRResidualTriangle crResiduals = createCrResiduals(paid, incurred);
        
        MclCorrelation lambda = new MclCorrelation(lrResiduals, crResiduals);
        assertEquals(INCURRED_LAMBDA, lambda.getValue(), TestConfig.EPSILON);
    }
}
