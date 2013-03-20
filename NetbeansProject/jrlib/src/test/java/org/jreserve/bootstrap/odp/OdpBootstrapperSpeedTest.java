package org.jreserve.bootstrap.odp;

import java.util.Collections;
import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.bootstrap.DefaultResidualGenerator;
import org.jreserve.bootstrap.JavaRandom;
import org.jreserve.estimate.ChainLadderEstimate;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.SimpleLinkRatio;
import org.jreserve.triangle.claim.CummulatedClaimTriangle;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class OdpBootstrapperSpeedTest {
    
    private final static int N = 100000;
    private final static double LIMIT = 5d;
    private ODPBootstrapper bootstrap;
    
    public OdpBootstrapperSpeedTest() {
    }

    @Before
    public void setUp() {
        LinkRatio lrs = new SimpleLinkRatio(TestData.getCummulatedTriangle(TestData.PAID));
        PearsonResidualClaimTriangle resSource = new PearsonResidualClaimTriangle(lrs);
        ConstantScaleODPResidualTriangle odpResiduals = new ConstantScaleODPResidualTriangle(resSource);
        
        DefaultResidualGenerator resGenerator = new DefaultResidualGenerator(new JavaRandom());
        resGenerator.initialize(odpResiduals, Collections.EMPTY_LIST);
        ResidualODPEstimateSimulator estimateSimulator = new ResidualODPEstimateSimulator(resGenerator, odpResiduals);
        
        ODPPseudoTriangle pseudoTriangle = new ODPPseudoTriangle(resGenerator, odpResiduals);
        
        ChainLadderEstimate estimate = new ChainLadderEstimate(new SimpleLinkRatio(new CummulatedClaimTriangle(pseudoTriangle)));
        estimate.detach();
        bootstrap = new ODPBootstrapper(estimate, N, estimateSimulator);
    }

    @Test
    public void testSpeed() {
        if(!JRLibTestUtl.EXECUTE_SPEED_TESTS)
            return;
        
        long begin = System.currentTimeMillis();
        
        bootstrap.run();
        
        long end = System.currentTimeMillis();
        
        double[] reserves = bootstrap.getReserves();
        int n=0;
        double mean = 0d;
        for(int i=0; i<N; i++) {
            double w = (double) n / (double)(++n);
            mean = w*mean + (1-w) * reserves[i];
        }
        
        long dif = end - begin;
        double seconds = (double)dif / 1000d;
        
        assertTrue(
                String.format("Bootstrapping %d times took %.3f seconds! Limit is %.3f seconds.", N, seconds, LIMIT), 
                seconds <= LIMIT);
    }
}
