package org.jreserve.jrlib.bootstrap.mcl;

import org.jreserve.jrlib.bootstrap.mcl.MclBootstrapEstimateBundle;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.bootstrap.FixedRandom;
import org.jreserve.jrlib.bootstrap.mack.DummyMackProcessSimulator;
import org.jreserve.jrlib.bootstrap.mack.MackProcessSimulator;
import org.jreserve.jrlib.bootstrap.mcl.pseudodata.MclPseudoData;
import org.jreserve.jrlib.bootstrap.mcl.pseudodata.MclResidualBundle;
import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.claimratio.SimpleClaimRatio;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.SimpleClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle;
import org.jreserve.jrlib.claimratio.scale.residuals.ClaimRatioResiduals;
import org.jreserve.jrlib.estimate.Estimate;
import org.jreserve.jrlib.estimate.mcl.MclCalculationBundle;
import org.jreserve.jrlib.estimate.mcl.MclCorrelation;
import org.jreserve.jrlib.estimate.mcl.MclEstimateBundle;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.linkratio.scale.SimpleLinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle;
import org.jreserve.jrlib.linkratio.scale.residuals.LinkRatioResiduals;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclBootstrapEstimateBundleTest {

    private Estimate paid;
    private Estimate incurred;
    private Estimate bsPaid;
    private Estimate bsIncurred;
    
    
    @Before
    public void setUp() {
        ClaimTriangle paidCik = TestData.getCummulatedTriangle(TestData.PAID);
        ClaimTriangle incurredCik = TestData.getCummulatedTriangle(TestData.INCURRED);
        
        LinkRatio paidLr = new SimpleLinkRatio(paidCik);
        LinkRatio incurredLr = new SimpleLinkRatio(incurredCik);
        
        LRResidualTriangle paidLrResiduals = new LinkRatioResiduals(new SimpleLinkRatioScale(paidLr));
        CRResidualTriangle paidCrResiduals = createCrResiduals(incurredCik, paidCik, incurredLr, paidLr);
        MclCorrelation paidLambda = new MclCorrelation(paidLrResiduals, paidCrResiduals);
        
        LRResidualTriangle incurredLrResiduals = new LinkRatioResiduals(new SimpleLinkRatioScale(incurredLr));
        CRResidualTriangle incurredCrResiduals = createCrResiduals(paidCik, incurredCik, paidLr, incurredLr);
        MclCorrelation incurredLambda = new MclCorrelation(incurredLrResiduals, incurredCrResiduals);
        MclCalculationBundle calcBundle = new MclCalculationBundle(paidLambda, incurredLambda);
        
        MclEstimateBundle bundle = new MclEstimateBundle(calcBundle);
        paid = bundle.getPaidEstimate();
        incurred = bundle.getIncurredEstimate();
        
        MclResidualBundle resBundle = new MclResidualBundle(paidLrResiduals, paidCrResiduals, incurredLrResiduals, incurredCrResiduals);
        MclPseudoData pseudoData = new MclPseudoData(new FixedRandom(), resBundle);
        MackProcessSimulator ps = new DummyMackProcessSimulator();
        MackProcessSimulator is = new DummyMackProcessSimulator();
        
        MclBootstrapEstimateBundle bsBundle = new MclBootstrapEstimateBundle(calcBundle, pseudoData, ps, is);
        bsPaid = bsBundle.getPaidEstimate();
        bsIncurred = bsBundle.getIncurredEstimate();
    }
    
    private CRResidualTriangle createCrResiduals(ClaimTriangle numerator, ClaimTriangle denumerator, LinkRatio lrN, LinkRatio lrD) {
        ClaimRatio crs = new SimpleClaimRatio(numerator, denumerator);
        ClaimRatioScale scales = new SimpleClaimRatioScale(crs);
        return new ClaimRatioResiduals(scales);
    }
    
    @Test
    public void testRecalculate() {
        int accidents = paid.getAccidentCount();
        int developments = paid.getDevelopmentCount();
        
        for(int a=0; a<accidents; a++) {
            for(int d=0; d<developments; d++) {
                double ePaid = getExpectedValue(paid, a, d);
                assertEquals(ePaid, bsPaid.getValue(a, d), TestConfig.EPSILON);

                double eIncurred = getExpectedValue(incurred, a, d);
                assertEquals(eIncurred, bsIncurred.getValue(a, d), TestConfig.EPSILON);
            }
        }
    }
    
    private double getExpectedValue(Estimate estimate, int accident, int development) {
        double value = estimate.getValue(accident, development);
        int observedD = estimate.getObservedDevelopmentCount(accident);
        return (development < observedD)? value : value + 1d;
    }
    
}
