package org.jreserve.jrlib.bootstrap.mcl;

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.bootstrap.mack.MackGammaProcessSimulator;
import org.jreserve.jrlib.bootstrap.mack.MackProcessSimulator;
import org.jreserve.jrlib.bootstrap.mcl.pseudodata.MclPseudoData;
import org.jreserve.jrlib.bootstrap.mcl.pseudodata.MclResidualBundle;
import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.claimratio.SimpleClaimRatio;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.SimpleClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle;
import org.jreserve.jrlib.claimratio.scale.residuals.ClaimRatioResiduals;
import org.jreserve.jrlib.estimate.mcl.MclCalculationBundle;
import org.jreserve.jrlib.estimate.mcl.MclCorrelation;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.SimpleLinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle;
import org.jreserve.jrlib.linkratio.scale.residuals.LinkRatioResiduals;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.util.MathUtil;
import org.jreserve.jrlib.util.random.JavaRandom;
import org.jreserve.jrlib.util.random.Random;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclBootstrapSpeedTest {
    private final static long SEED = 100;
    private final static int N = 100000;
    private final static double LIMIT = 30d;
    private final static long TIMEOUT = 2L * ((long)LIMIT * 1000L);
    private MclBootstrapper bootstrap;

    @BeforeClass
    public static void setUpClass() {
        org.junit.Assume.assumeTrue("Mcl-Bootstrapper speed test skipped...", TestConfig.EXECUTE_SPEED_TESTS);
    }

    @Before
    public void setUp() {
        ClaimTriangle paidCik = TestData.getCummulatedTriangle(TestData.PAID);
        ClaimTriangle incurredCik = TestData.getCummulatedTriangle(TestData.INCURRED);

        LinkRatio paidLr = new SimpleLinkRatio(paidCik);
        LinkRatio incurredLr = new SimpleLinkRatio(incurredCik);
        
        LinkRatioScale paidScales = new SimpleLinkRatioScale(paidLr);
        LinkRatioScale incurredScales = new SimpleLinkRatioScale(incurredLr);
        
        LRResidualTriangle paidLrResiduals = new LinkRatioResiduals(paidScales);
        LRResidualTriangle incurredLrResiduals = new LinkRatioResiduals(incurredScales);
        CRResidualTriangle paidCrResiduals = createCrResiduals(incurredCik, paidCik, incurredLr, paidLr);
        CRResidualTriangle incurredCrResiduals = createCrResiduals(paidCik, incurredCik, paidLr, incurredLr);

        MclCorrelation paidLambda = new MclCorrelation(paidLrResiduals, paidCrResiduals);
        MclCorrelation incurredLambda = new MclCorrelation(incurredLrResiduals, incurredCrResiduals);
        MclCalculationBundle calcBundle = new MclCalculationBundle(paidLambda, incurredLambda);
        
        MclResidualBundle resBundle = new MclResidualBundle(paidLrResiduals, paidCrResiduals, incurredLrResiduals, incurredCrResiduals);
        Random rnd = new JavaRandom(SEED);
        MclPseudoData pseudoData = new MclPseudoData(rnd, resBundle);
        MackProcessSimulator ps = new MackGammaProcessSimulator(rnd, paidScales);
        MackProcessSimulator is = new MackGammaProcessSimulator(rnd, incurredScales);
        
        MclBootstrapEstimateBundle extimate = new MclBootstrapEstimateBundle(calcBundle, pseudoData, ps, is);
        bootstrap = new MclBootstrapper(extimate, N);
    }    
    
    private CRResidualTriangle createCrResiduals(ClaimTriangle numerator, ClaimTriangle denumerator, LinkRatio lrN, LinkRatio lrD) {
        ClaimRatio crs = new SimpleClaimRatio(numerator, denumerator);
        ClaimRatioScale scales = new SimpleClaimRatioScale(crs);
        return new ClaimRatioResiduals(scales);
    }


    @Test(timeout=TIMEOUT)
    public void testSpeed() {
        System.out.println("Begin MCL-Bootstrapper speed test.\n\tTimeout: "+(TIMEOUT/1000));
        
        long begin = System.currentTimeMillis();
        bootstrap.run();
        long dif = System.currentTimeMillis() - begin;
        double seconds = (double)dif / 1000d;
        assertTrue(
                String.format("MCL-Bootstrapping %d times took %.3f seconds! Limit is %.3f seconds.", N, seconds, LIMIT), 
                seconds <= LIMIT);
        
        double paidMean = calculateMean(bootstrap.getPaidReserves());
        double incurredMean = calculateMean(bootstrap.getIncurredReserves());
        
        System.out.printf("MCL-Bootstrapping %d times took %.3f seconds.%n\tPaid-reserve: %.0f%n\tIncurred-reserve: %.0f%n", N, seconds, paidMean, incurredMean);
    }
    
    private double calculateMean(double[][] reserves) {
        int n=0;
        double mean = 0d;
        for(int i=0; i<N; i++) {
            double w = (double) n / (double)(++n);
            double r = MathUtil.sum(reserves[i]);
            mean = w*mean + (1-w) * r;
        }
        return mean;
    }
}
