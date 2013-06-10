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
package org.jreserve.jrlib.bootstrap.mack;

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.bootstrap.EstimateBootstrapper;
import org.jreserve.jrlib.bootstrap.util.BootstrapUtil;
import org.jreserve.jrlib.bootstrap.util.HistogramData;
import org.jreserve.jrlib.bootstrap.util.HistogramDataFactory;
import org.jreserve.jrlib.estimate.ChainLadderEstimate;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.linkratio.curve.ExponentialLRCurve;
import org.jreserve.jrlib.linkratio.curve.LinkRatioSmoothing;
import org.jreserve.jrlib.linkratio.curve.SimpleLinkRatioSmoothing;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.SimpleLinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.residuals.AdjustedLinkRatioResiduals;
import org.jreserve.jrlib.linkratio.scale.residuals.CenteredLinkRatioResiduals;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangleCorrection;
import org.jreserve.jrlib.linkratio.scale.residuals.LinkRatioResiduals;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
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
public class MackBootstrapSpeedTest {
    
    private final static long SEED = 100;
    private final static int N = 100000;
    private final static double LIMIT = 6d;
    private final static long TIMEOUT = 2L * ((long)(LIMIT * 1000));
    
    private EstimateBootstrapper<FactorTriangle> bootstrap;
    private double mean;
    
    @BeforeClass
    public static void setUpClass() {
        org.junit.Assume.assumeTrue("Mack-Bootstrapper speed test skipped...", TestConfig.EXECUTE_SPEED_TESTS);
    }
    
    @Before
    public void setUp() {
        Random rnd = new JavaRandom(SEED);
        
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.PAID);
        
        LinkRatio lrs = new SimpleLinkRatio(cik);
        lrs = new SimpleLinkRatioSmoothing(lrs, new ExponentialLRCurve());
        ((LinkRatioSmoothing)lrs).setDevelopmentCount(10);
        mean = calculateReserve(lrs);
        
        LinkRatioScale scales = new SimpleLinkRatioScale(lrs); //MinMaxScaleEstimate
        LRResidualTriangle residuals = createResiduals(scales);
        
        MackPseudoFactorTriangle pseudoFik = new MackPseudoFactorTriangle(rnd, residuals);
        MackGammaProcessSimulator procSim = new MackGammaProcessSimulator(rnd, scales);
        MackBootstrapEstimate mackEstimate = new MackBootstrapEstimate(lrs, procSim);
        lrs.setSource(pseudoFik);
        bootstrap = new EstimateBootstrapper<FactorTriangle>(pseudoFik, N, mackEstimate);
    }

    private double calculateReserve(LinkRatio lrs) {
        ChainLadderEstimate e = new ChainLadderEstimate(lrs);
        double reserve = e.getReserve();
        e.detach();
        return reserve;
    }
    
    private LRResidualTriangle createResiduals(LinkRatioScale scales) {
        LRResidualTriangle residuals = new LinkRatioResiduals(scales);
        residuals = new LRResidualTriangleCorrection(residuals, 0, 6, Double.NaN);
        residuals = new AdjustedLinkRatioResiduals(residuals);
        return new CenteredLinkRatioResiduals(residuals);
    }

    @Test(timeout=TIMEOUT)
    public void testSpeed() {
        System.out.println("Begin Mack-Bootstrapper speed test.\n\tTimeout: "+(TIMEOUT/1000));
        
        long begin = System.currentTimeMillis();
        
        bootstrap.run();
        
        long end = System.currentTimeMillis();
        
        double[][] reserves = bootstrap.getReserves();
        
        long dif = end - begin;
        double seconds = (double)dif / 1000d;
        
        double bsMean = BootstrapUtil.getMeanTotalReserve(reserves);
        //printHistogram(reserves);
        
        System.out.printf("Mack-Bootstrapping %d times took %.3f seconds.%n\tReserve: %.0f%n\tOriginal: %.0f%n", N, seconds, bsMean, mean);
        assertTrue(
                String.format("Mack-Bootstrapping %d times took %.3f seconds! Limit is %.3f seconds.", N, seconds, LIMIT), 
                seconds <= LIMIT);
    }
    
    private void printHistogram(double[][] reserves) {
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
