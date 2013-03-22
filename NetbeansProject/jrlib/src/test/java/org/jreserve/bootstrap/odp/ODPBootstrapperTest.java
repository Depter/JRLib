package org.jreserve.bootstrap.odp;

import org.jreserve.bootstrap.ProcessSimulator;
import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.estimate.ChainLadderEstimate;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.SimpleLinkRatio;
import org.jreserve.util.MathUtil;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class ODPBootstrapperTest {

    private final static int N = 1;
    
    private ODPBootstrapper bootstrap;
    private ChainLadderEstimate estimate;
    
    public ODPBootstrapperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        LinkRatio lrs = new SimpleLinkRatio(TestData.getCummulatedTriangle(TestData.PAID));
        estimate = new ChainLadderEstimate(lrs);
        estimate.detach();
        bootstrap = new ODPBootstrapper(estimate, N, new DummyEstimateSimulator());
    }

    @Test
    public void testCalculatePseudoReserve() {
        double[][] reserves = new double[N][];
        for(int i=0; i<N; i++)
            reserves[i] = bootstrap.calculatePseudoReserve();
        
        double expected = estimate.getReserve();
        for(int i=0; i<N; i++) {
            double found = MathUtil.sum(reserves[i], false);
            assertEquals(expected, found, JRLibTestUtl.EPSILON);
        }
        
    }
    
    private static class DummyEstimateSimulator implements ProcessSimulator {
        public double simulateEstimate(double cik, int accident, int development) {
            return cik;
        }
    }

}