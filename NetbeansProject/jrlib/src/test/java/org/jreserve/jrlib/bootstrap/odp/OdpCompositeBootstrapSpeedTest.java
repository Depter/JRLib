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
package org.jreserve.jrlib.bootstrap.odp;

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.bootstrap.EstimateBootstrapper;
import org.jreserve.jrlib.bootstrap.odp.scale.DefaultOdpResidualScaleSelection;
import org.jreserve.jrlib.bootstrap.odp.scale.OdpResidualScale;
import org.jreserve.jrlib.bootstrap.odp.scale.UserInputOdpSMethod;
import org.jreserve.jrlib.bootstrap.odp.scale.VariableOdpResidualScale;
import org.jreserve.jrlib.bootstrap.odp.scaledresiduals.DefaultOdpScaledResidualTriangle;
import org.jreserve.jrlib.bootstrap.odp.scaledresiduals.OdpScaledResidualTriangle;
import org.jreserve.jrlib.bootstrap.odp.scaledresiduals.OdpScaledResidualTriangleCorrection;
import org.jreserve.jrlib.bootstrap.util.BootstrapUtil;
import org.jreserve.jrlib.bootstrap.util.HistogramData;
import org.jreserve.jrlib.bootstrap.util.HistogramDataFactory;
import org.jreserve.jrlib.estimate.CapeCodEstimate;
import org.jreserve.jrlib.estimate.ChainLadderEstimate;
import org.jreserve.jrlib.estimate.ProcessSimulatorCompositeEstimate;
import org.jreserve.jrlib.estimate.ProcessSimulatorEstimate;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.linkratio.curve.ExponentialLRCurve;
import org.jreserve.jrlib.linkratio.curve.LinkRatioSmoothing;
import org.jreserve.jrlib.linkratio.curve.SimpleLinkRatioSmoothing;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.util.random.JavaRandom;
import org.jreserve.jrlib.util.random.Random;
import org.jreserve.jrlib.vector.InputVector;
import org.jreserve.jrlib.vector.Vector;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class OdpCompositeBootstrapSpeedTest {
    
    private final static long SEED = 100;
    private final static int N = 100000;
    private final static double LIMIT = 7d;
    private final static long TIMEOUT = 2L * ((long)LIMIT * 1000L);
    
    private EstimateBootstrapper<ClaimTriangle> bootstrap;
    private double mean;
    
    @BeforeClass
    public static void setUpClass() {
        org.junit.Assume.assumeTrue("Composite-ODP-Bootstrapper speed test skipped...", TestConfig.EXECUTE_SPEED_TESTS);
    }

    @Before
    public void setUp() {
        Random rnd = new JavaRandom(SEED);
        
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.PAID);
        LinkRatio lrs = new SimpleLinkRatio(cik);
        
        lrs = new SimpleLinkRatioSmoothing(lrs, new ExponentialLRCurve());
        ((LinkRatioSmoothing)lrs).setDevelopmentCount(10);
        
        ProcessSimulatorEstimate estimate = createEstimate(lrs);
        mean = estimate.getReserve();
        
        OdpResidualScale scales = getResidualScale(lrs);
        OdpScaledResidualTriangle residuals = new DefaultOdpScaledResidualTriangle(scales);
        residuals = new OdpScaledResidualTriangleCorrection(residuals, 0, 7, Double.NaN);
        residuals = new OdpScaledResidualTriangleCorrection(residuals, 7, 0, Double.NaN);
        
        OdpPseudoClaimTriangle pseudoCik = new OdpPseudoClaimTriangle(rnd, residuals);
        lrs.getSourceFactors().setSource(pseudoCik);
        OdpGammaProcessSimulator procSim = new OdpGammaProcessSimulator(rnd, residuals.getSourceOdpResidualScales());
        estimate.setProcessSimulator(procSim);
        
        bootstrap = new EstimateBootstrapper<ClaimTriangle>(pseudoCik, N, estimate);
    }
    
    private ProcessSimulatorEstimate createEstimate(LinkRatio lrs) {
        ChainLadderEstimate cl = new ChainLadderEstimate(lrs);
        Vector exposure = new InputVector(TestData.getCachedVector(TestData.EXPOSURE));
        CapeCodEstimate cc = new CapeCodEstimate(lrs, exposure);
        return new ProcessSimulatorCompositeEstimate(
                cl, cl, cl, cl, cl, cl,
                cc, cc
                );
    }
    
    private OdpResidualScale getResidualScale(LinkRatio lrs) {
        VariableOdpResidualScale scales = new VariableOdpResidualScale(lrs);
        DefaultOdpResidualScaleSelection sel = new DefaultOdpResidualScaleSelection(scales);
        sel.setMethod(new UserInputOdpSMethod(7, 172), 7);
        return sel;
    }
    
    @Test(timeout=TIMEOUT)
    public void testSpeed() {
        System.out.println("Begin Composite-ODP-Bootstrapper speed test.\n\tTimeout: "+(TIMEOUT/1000));
        
        long begin = System.currentTimeMillis();
        
        bootstrap.run();
        
        long end = System.currentTimeMillis();
        
        double[][] reserves = bootstrap.getReserves();
        
        long dif = end - begin;
        double seconds = (double)dif / 1000d;
        
        double bsMean = BootstrapUtil.getMeanTotalReserve(reserves);
        //printHistogram(reserves);
        
        System.out.printf("Composite-ODP-Bootstrapping %d times took %.3f seconds.%n\t Reserve: %.0f%n\tOriginal: %.0f%n", N, seconds, bsMean, mean);
        assertTrue(
                String.format("Composite-ODP-Bootstrapping %d times took %.3f seconds! Limit is %.3f seconds.", N, seconds, LIMIT), 
                seconds <= LIMIT);
    }
    
    private void printHistogram(double[][] reserves) {
        double[] total = BootstrapUtil.getTotalReserves(reserves);
        BootstrapUtil.scaleAdjustment(total, mean);
        
        double firstUpper = 1850000;
        double interval = 60000;
        HistogramData data = new HistogramDataFactory(total).setIntervals(firstUpper, interval).buildData();
        //HistogramData data = new HistogramDataFactory(total).buildData();
        
        int count = data.getIntervalCount();
        System.out.println("Interval\tLower\tUpper\tCount");
        String fromat = "%d\t%f\t%f\t%d%n";
        for(int i=0; i<count; i++)
            System.out.printf(fromat, i, data.getLowerBound(i), data.getUpperBound(i), data.getCount(i));
    }    
}
