package org.jreserve.bootstrap.odp;

import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.bootstrap.JavaRandom;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.SimpleLinkRatio;
import org.jreserve.util.MathUtil;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class OdpBootstrapperSpeedTest {
    
    private final static long SEED = 100;
    private final static int N = 100000;
    private final static double LIMIT = 5d;
    private ODPBootstrapper bootstrap;
    
    public OdpBootstrapperSpeedTest() {
    }

    @Before
    public void setUp() {
        LinkRatio lrs = new SimpleLinkRatio(TestData.getCummulatedTriangle(TestData.PAID));
        bootstrap = new ODPBootstrapFactory()
                .setLinkRatios(lrs).setBootstrapCount(N).setRandomGenerator(new JavaRandom(SEED))
                .useConstantScaledResiduals().useGammaProcessSimulator().build();
    }

    @Test
    public void testSpeed() {
        if(!JRLibTestUtl.EXECUTE_SPEED_TESTS) {
            System.out.println("OdpBootstrapperSpeedTest skipped...");
            return;
        }
        System.out.println("Begin OdpBootstrapperSpeedTest.");
        
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
        
        System.out.printf("Bootstrapping %d times took %.3f seconds.%n\t Reserve: %.0f%n", N, seconds, mean);
        assertTrue(
                String.format("Bootstrapping %d times took %.3f seconds! Limit is %.3f seconds.", N, seconds, LIMIT), 
                seconds <= LIMIT);
    }
}