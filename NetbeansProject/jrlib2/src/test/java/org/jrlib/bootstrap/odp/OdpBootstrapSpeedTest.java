package org.jrlib.bootstrap.odp;

import org.jrlib.TestConfig;
import org.jrlib.TestData;
import org.jrlib.bootstrap.EstimateBootstrapper;
import org.jrlib.bootstrap.odp.residuals.DefaultOdpScaledResidualTriangle;
import org.jrlib.bootstrap.odp.residuals.OdpScaledResidualTriangle;
import org.jrlib.linkratio.LinkRatio;
import org.jrlib.linkratio.SimpleLinkRatio;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.util.MathUtil;
import org.jrlib.util.random.JavaRandom;
import org.jrlib.util.random.Random;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class OdpBootstrapSpeedTest {
    
    private final static long SEED = 100;
    private final static int N = 100000;
    private final static double LIMIT = 5d;
    private final static long TIMEOUT = 2L * ((long)LIMIT * 1000L);
    private EstimateBootstrapper<OdpEstimate> bootstrap;

    @Before
    public void setUp() {
        Random rnd = new JavaRandom(SEED);
        
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.PAID);
        LinkRatio lrs = new SimpleLinkRatio(cik);
        OdpScaledResidualTriangle residuals = new DefaultOdpScaledResidualTriangle(lrs); //Adjusted, Cosntant-Scale
        
        OdpPseudoClaimTriangle pseudoCik = new OdpPseudoClaimTriangle(rnd, residuals);
        OdpGammaProcessSimulator procSim = new OdpGammaProcessSimulator(rnd, residuals.getSourceOdpResidualScales());
        OdpEstimate odpEstimate = new OdpEstimate(lrs, procSim);
        lrs.getSourceFactors().setSource(pseudoCik);
        bootstrap = new EstimateBootstrapper<OdpEstimate>(odpEstimate, N);
    }

    @Test(timeout=TIMEOUT)
    public void testSpeed() {
        if(!TestConfig.EXECUTE_SPEED_TESTS) {
            System.out.println("ODP-Bootstrapper speed test skipped...");
            return;
        }
        System.out.println("Begin ODP-Bootstrapper speed test.");
        
        long begin = System.currentTimeMillis();
        
        bootstrap.run();
        
        long end = System.currentTimeMillis();
        
        double[][] reserves = bootstrap.getReserves();
        int n=0;
        double mean = 0d;
        for(int i=0; i<N; i++) {
            double w = (double) n / (double)(++n);
            double r = MathUtil.sum(reserves[i]);
            mean = w*mean + (1-w) * r;
        }
        
        long dif = end - begin;
        double seconds = (double)dif / 1000d;
        
        System.out.printf("ODP-Bootstrapping %d times took %.3f seconds.%n\t Reserve: %.0f%n", N, seconds, mean);
        assertTrue(
                String.format("ODP-Bootstrapping %d times took %.3f seconds! Limit is %.3f seconds.", N, seconds, LIMIT), 
                seconds <= LIMIT);
    }
}
