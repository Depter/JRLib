package org.jreserve.jrlib.bootstrap.mcl;

import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.bootstrap.mack.MackGammaProcessSimulator;
import org.jreserve.jrlib.bootstrap.mack.MackProcessSimulator;
import org.jreserve.jrlib.bootstrap.mcl.pseudodata.MclPseudoData;
import org.jreserve.jrlib.bootstrap.mcl.pseudodata.MclResidualBundle;
import org.jreserve.jrlib.bootstrap.util.BootstrapUtil;
import org.jreserve.jrlib.bootstrap.util.HistogramData;
import org.jreserve.jrlib.bootstrap.util.HistogramDataFactory;
import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.claimratio.DefaultClaimRatioSelection;
import org.jreserve.jrlib.claimratio.LrCrExtrapolation;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.SimpleClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.residuals.AdjustedClaimRatioResiduals;
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle;
import org.jreserve.jrlib.claimratio.scale.residuals.ClaimRatioResidualTriangleCorrection;
import org.jreserve.jrlib.claimratio.scale.residuals.ClaimRatioResiduals;
import org.jreserve.jrlib.estimate.mcl.MclCalculationBundle;
import org.jreserve.jrlib.estimate.mcl.MclCorrelation;
import org.jreserve.jrlib.estimate.mcl.MclEstimateBundle;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.linkratio.curve.ExponentialLRCurve;
import org.jreserve.jrlib.linkratio.curve.LinkRatioSmoothing;
import org.jreserve.jrlib.linkratio.curve.SimpleLinkRatioSmoothing;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.SimpleLinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangleCorrection;
import org.jreserve.jrlib.linkratio.scale.residuals.LinkRatioResiduals;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.DefaultRatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;
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
    private final static int N = 10;//100000;
    private final static double LIMIT = 30d;
    private final static long TIMEOUT = 2L * ((long)LIMIT * 1000L);
    
    private MclBootstrapper bootstrap;
    private double paidMean;
    private double incurredMean;
    
    @BeforeClass
    public static void setUpClass() {
        //org.junit.Assume.assumeTrue("Mcl-Bootstrapper speed test skipped...", TestConfig.EXECUTE_SPEED_TESTS);
    }

    @Before
    public void setUp() {
        ClaimTriangle paidCik = TestData.getCummulatedTriangle(TestData.PAID);
        ClaimTriangle incurredCik = TestData.getCummulatedTriangle(TestData.INCURRED);

        LinkRatio paidLr = createLinkRatio(paidCik);
        LinkRatio incurredLr = createLinkRatio(incurredCik);
        
        LinkRatioScale paidScales = new SimpleLinkRatioScale(paidLr);
        LinkRatioScale incurredScales = new SimpleLinkRatioScale(incurredLr);
        
        LRResidualTriangle paidLrResiduals =     createLrResiduals(paidScales);
        LRResidualTriangle incurredLrResiduals = createLrResiduals(incurredScales);
        CRResidualTriangle paidCrResiduals =     createCrResiduals(incurredLr, paidLr);
        CRResidualTriangle incurredCrResiduals = createCrResiduals(paidLr, incurredLr);
        
        createMeans(paidLrResiduals, paidCrResiduals, incurredLrResiduals, incurredCrResiduals);
        createBs(paidLrResiduals, paidCrResiduals, incurredLrResiduals, incurredCrResiduals);
    }    
    
    private LRResidualTriangle createLrResiduals(LinkRatioScale scales) {
        LRResidualTriangle residuals = new LinkRatioResiduals(scales);
        residuals = new LRResidualTriangleCorrection(residuals, 0, 6, Double.NaN);
        residuals = new LRResidualTriangleCorrection(residuals, 6, 0, Double.NaN);
        return residuals;
    }
    
    private LinkRatio createLinkRatio(ClaimTriangle claims) {
        LinkRatio lrs = new SimpleLinkRatio(claims);
        lrs = new SimpleLinkRatioSmoothing(lrs, new ExponentialLRCurve());
        ((LinkRatioSmoothing)lrs).setDevelopmentCount(10);
        return lrs;
    }
    
    private CRResidualTriangle createCrResiduals(LinkRatio lrN, LinkRatio lrD) {
        ClaimRatio crs = createClaimRatio(lrN, lrD);
        ClaimRatioScale scales = new SimpleClaimRatioScale(crs);
        CRResidualTriangle res = new ClaimRatioResiduals(scales);
        return adjustResiduals(res);
    }

    private ClaimRatio createClaimRatio(LinkRatio numerator, LinkRatio denominator) {
        RatioTriangle ratios = new  DefaultRatioTriangle(numerator.getSourceTriangle(), denominator.getSourceTriangle());
        LrCrExtrapolation method = new LrCrExtrapolation(numerator, denominator);
        DefaultClaimRatioSelection crs = new DefaultClaimRatioSelection(ratios);
        int length = numerator.getLength();
        crs.setDevelopmentCount(length);
        
        for(int i=ratios.getDevelopmentCount(); i<length; i++) 
            crs.setMethod(method, i);
        
        return crs;
    }
    
    private CRResidualTriangle adjustResiduals(CRResidualTriangle res) {
        int accidents = res.getAccidentCount();
        for(int a=0; a<accidents; a++)
            res = new ClaimRatioResidualTriangleCorrection(res, a, res.getDevelopmentCount(a)-1, Double.NaN);
        return new AdjustedClaimRatioResiduals(res);
    }
    
    private void createMeans(LRResidualTriangle paidLr, CRResidualTriangle paidCr, LRResidualTriangle incurredLr, CRResidualTriangle incurredCr) {
        MclCorrelation paidLambda = new MclCorrelation(paidLr, paidCr);
        MclCorrelation incurredLambda = new MclCorrelation(incurredLr, incurredCr);
        MclCalculationBundle calcBundle = new MclCalculationBundle(paidLambda, incurredLambda);
        MclEstimateBundle eb = new MclEstimateBundle(calcBundle, false);
        paidMean = eb.getPaidEstimate().getReserve();
        incurredMean = eb.getIncurredEstimate().getReserve();
    }
    
    private void createBs(LRResidualTriangle paidLr, CRResidualTriangle paidCr, LRResidualTriangle incurredLr, CRResidualTriangle incurredCr) {
        MclResidualBundle resBundle = new MclResidualBundle(paidLr, paidCr, incurredLr, incurredCr);
        Random rnd = new JavaRandom(SEED);
        MclPseudoData pseudoData = new MclPseudoData(rnd, resBundle);
        MackProcessSimulator ps = new MackGammaProcessSimulator(rnd, paidLr.getSourceLinkRatioScales());
        MackProcessSimulator is = new MackGammaProcessSimulator(rnd, incurredLr.getSourceLinkRatioScales());
        
        MclBootstrapEstimateBundle estimate = new MclBootstrapEstimateBundle(pseudoData, ps, is);
        bootstrap = new MclBootstrapper(estimate, N);
    }

    @Test//(timeout=TIMEOUT)
    public void testSpeed() {
        System.out.println("Begin MCL-Bootstrapper speed test.\n\tTimeout: "+(TIMEOUT/1000));
        
        long begin = System.currentTimeMillis();
        bootstrap.run();
        long dif = System.currentTimeMillis() - begin;
        double seconds = (double)dif / 1000d;
        assertTrue(
                String.format("MCL-Bootstrapping %d times took %.3f seconds! Limit is %.3f seconds.", N, seconds, LIMIT), 
                seconds <= LIMIT);
        
        double bsPaidMean = BootstrapUtil.getMeanTotalReserve(bootstrap.getPaidReserves());
        double bsIncurredMean = BootstrapUtil.getMeanTotalReserve(bootstrap.getIncurredReserves());
        printHistogram(bootstrap.getPaidReserves(), paidMean);
        
        String msg = "MCL-Bootstrapping %d times took %.3f seconds.%n";
        msg += "\tPaid-reserve:%n\t\tBootstrap: %.0f%n\t\tOriginal: %.0f%n";
        msg += "\tIncurred-reserve:%n\t\tBootstrap: %.0f%n\t\tOriginal: %.0f%n";
        System.out.printf(msg, N, seconds, bsPaidMean, paidMean, bsIncurredMean, incurredMean);
    }
    
    private void printHistogram(double[][] reserves, double mean) {
        double[] total = BootstrapUtil.getTotalReserves(reserves);
        BootstrapUtil.scaleAdjustment(total, mean);
        double firstUpper = 1850000;
        double interval = 60000;
        HistogramData data = new HistogramDataFactory(total).setIntervals(firstUpper, interval).buildData();
        //HistogramData data = new HistogramDataFactory(total).setIntervalCount(50).buildData();
        
        int count = data.getIntervalCount();
        System.out.println("Interval\tLower\tUpper\tCount");
        String fromat = "%d\t%f\t%f\t%d%n";
        for(int i=0; i<count; i++)
            System.out.printf(fromat, i, data.getLowerBound(i), data.getUpperBound(i), data.getCount(i));
    }
}
