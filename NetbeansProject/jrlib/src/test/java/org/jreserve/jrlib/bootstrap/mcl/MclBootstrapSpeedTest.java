/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.jrlib.bootstrap.mcl;

import java.util.Collections;
import java.util.List;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.bootstrap.mcl.pseudodata.MclPseudoData;
import org.jreserve.jrlib.bootstrap.mcl.pseudodata.MclResidualBundle;
import org.jreserve.jrlib.bootstrap.util.BootstrapUtil;
import org.jreserve.jrlib.bootstrap.util.HistogramData;
import org.jreserve.jrlib.bootstrap.util.HistogramDataFactory;
import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.claimratio.DefaultClaimRatioSelection;
import org.jreserve.jrlib.claimratio.LrCrExtrapolation;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScaleInput;
import org.jreserve.jrlib.claimratio.scale.DefaultClaimRatioScaleSelection;
import org.jreserve.jrlib.claimratio.scale.residuals.AdjustedClaimRatioResiduals;
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle;
import org.jreserve.jrlib.claimratio.scale.residuals.CenteredClaimRatioResiduals;
import org.jreserve.jrlib.claimratio.scale.residuals.ClaimRatioResiduals;
import org.jreserve.jrlib.estimate.mcl.MclCalculationBundle;
import org.jreserve.jrlib.estimate.mcl.MclCorrelation;
import org.jreserve.jrlib.estimate.mcl.MclEstimateBundle;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.linkratio.curve.ExponentialLRCurve;
import org.jreserve.jrlib.linkratio.curve.LinkRatioSmoothing;
import org.jreserve.jrlib.linkratio.curve.SimpleLinkRatioSmoothing;
import org.jreserve.jrlib.linkratio.scale.DefaultLinkRatioScaleSelection;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScaleInput;
import org.jreserve.jrlib.linkratio.scale.residuals.AdjustedLinkRatioResiduals;
import org.jreserve.jrlib.linkratio.scale.residuals.CenteredLinkRatioResiduals;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle;
import org.jreserve.jrlib.linkratio.scale.residuals.LinkRatioResiduals;
import org.jreserve.jrlib.scale.UserInputScaleEstimator;
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
    private final static int N = 100000;
    private final static double LIMIT = 30d;
    private final static long TIMEOUT = 2L * ((long)LIMIT * 1000L);
    
    private MclBootstrapper bootstrap;
    private double paidMean;
    private double incurredMean;
    
    @BeforeClass
    public static void setUpClass() {
        org.junit.Assume.assumeTrue("Mcl-Bootstrapper speed test skipped...", TestConfig.EXECUTE_SPEED_TESTS);
    }

    @Before
    public void setUp() {
        ClaimTriangle paidCik = TestData.getCummulatedTriangle(TestData.PAID);
        ClaimTriangle incurredCik = TestData.getCummulatedTriangle(TestData.INCURRED);

        LinkRatio paidLr = createLinkRatio(paidCik);
        LinkRatio incurredLr = createLinkRatio(incurredCik);
        
        LinkRatioScale paidScales = createLRScale(paidLr, 3.84, 2.51, 1.65, 1.08); //lr regression
        LinkRatioScale incurredScales = createLRScale(incurredLr, 1.45, 0.67, 0.31, 0.14);
        
        LRResidualTriangle paidLrResiduals =     createLrResiduals(paidScales);
        LRResidualTriangle incurredLrResiduals = createLrResiduals(incurredScales);
        
        ClaimRatio paidCr = createClaimRatio(incurredLr, paidLr);
        ClaimRatioScale paidCrScales = createClaimRatioScale(paidCr, 91.88, 84.41, 77.55);
        CRResidualTriangle paidCrResiduals =     createCrResiduals(paidCrScales);
        
        ClaimRatio incurredCr = createClaimRatio(paidLr, incurredLr);
        ClaimRatioScale incurredCrScales = createClaimRatioScale(incurredCr, 86.54, 81.74, 77.2);
        CRResidualTriangle incurredCrResiduals = createCrResiduals(incurredCrScales);
        
        createMeans(paidLrResiduals, paidCrResiduals, incurredLrResiduals, incurredCrResiduals);
        createBs(paidLrResiduals, paidCrResiduals, incurredLrResiduals, incurredCrResiduals);
    }    
    
    private LinkRatio createLinkRatio(ClaimTriangle claims) {
        LinkRatio lrs = new SimpleLinkRatio(claims);
        lrs = new SimpleLinkRatioSmoothing(lrs, new ExponentialLRCurve());
        ((LinkRatioSmoothing)lrs).setDevelopmentCount(10);
        return lrs;
    }
    
    private LinkRatioScale createLRScale(LinkRatio lr, double t1, double t2, double t3, double t4) {
        UserInputScaleEstimator<LinkRatioScaleInput> method = new UserInputScaleEstimator<LinkRatioScaleInput>();
        method.setValue(6, t1);
        method.setValue(7, t2);
        method.setValue(8, t3);
        method.setValue(9, t4);
        DefaultLinkRatioScaleSelection scale = new DefaultLinkRatioScaleSelection(lr);
        scale.setMethod(method, 6, 7, 8, 9);
        return scale;
    }
    
    private LRResidualTriangle createLrResiduals(LinkRatioScale scales) {
        LRResidualTriangle res = new LinkRatioResiduals(scales);
        res = new AdjustedLinkRatioResiduals(res);
        return new CenteredLinkRatioResiduals(res);
    }
    
    private CRResidualTriangle createCrResiduals(ClaimRatioScale scales) {
        CRResidualTriangle res = new ClaimRatioResiduals(scales);
        res = new AdjustedClaimRatioResiduals(res);
        return new CenteredClaimRatioResiduals(res);
    }
    
    private ClaimRatioScale createClaimRatioScale(ClaimRatio crs, double t1, double t2, double t3) {
        DefaultClaimRatioScaleSelection scales = new DefaultClaimRatioScaleSelection(crs);
        UserInputScaleEstimator<ClaimRatioScaleInput> m = new UserInputScaleEstimator<ClaimRatioScaleInput>();
        m.setValue(7, t1);
        m.setValue(8, t2);
        m.setValue(9, t3);
        scales.setMethod(m, 7, 8, 9);
        return scales;
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
    
    private void createMeans(LRResidualTriangle paidLr, CRResidualTriangle paidCr, LRResidualTriangle incurredLr, CRResidualTriangle incurredCr) {
        MclCorrelation paidLambda = new MclCorrelation(paidLr, paidCr);
        MclCorrelation incurredLambda = new MclCorrelation(incurredLr, incurredCr);
        MclCalculationBundle calcBundle = new MclCalculationBundle(paidLambda, incurredLambda);
        MclEstimateBundle eb = new MclEstimateBundle(calcBundle);
        paidMean = eb.getPaidEstimate().getReserve();
        incurredMean = eb.getIncurredEstimate().getReserve();
    }
    
    private void createBs(LRResidualTriangle paidLr, CRResidualTriangle paidCr, LRResidualTriangle incurredLr, CRResidualTriangle incurredCr) {
        Random rnd = new JavaRandom(SEED);
        MclProcessSimulator ps = new SimpleGammaMclProcessSimulator(paidLr.getSourceLinkRatioScales(), rnd, true);
        MclProcessSimulator is = new SimpleGammaMclProcessSimulator(incurredLr.getSourceLinkRatioScales(), rnd, false);
        
        MclResidualBundle resBundle = new MclResidualBundle(paidLr, paidCr, incurredLr, incurredCr);
        MclPseudoData pseudoData = new MclPseudoData(rnd, resBundle, createSegments());
        
        MclBootstrapEstimateBundle estimate = new MclBootstrapEstimateBundle(pseudoData, ps, is);
        bootstrap = new MclBootstrapper(estimate, N);
    }
    
    private List<int[][]> createSegments() {
        return Collections.singletonList(new int[][] {{0, 6}});
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
        
        double bsPaidMean = BootstrapUtil.getMeanTotalReserve(bootstrap.getPaidReserves());
        double bsIncurredMean = BootstrapUtil.getMeanTotalReserve(bootstrap.getIncurredReserves());
        //printHistogram(bootstrap.getPaidReserves(), paidMean);
        //printHistogram(bootstrap.getIncurredReserves(), incurredMean);
        
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
        //HistogramData data = new HistogramDataFactory(total).setIntervals(firstUpper, interval).buildData();
        HistogramData data = new HistogramDataFactory(total).setIntervalCount(50).buildData();
        
        int count = data.getIntervalCount();
        System.out.println("Interval\tLower\tUpper\tCount");
        String fromat = "%d\t%f\t%f\t%d%n";
        for(int i=0; i<count; i++)
            System.out.printf(fromat, i, data.getLowerBound(i), data.getUpperBound(i), data.getCount(i));
    }
}
