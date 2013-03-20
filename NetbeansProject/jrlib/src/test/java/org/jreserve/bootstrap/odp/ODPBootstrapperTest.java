package org.jreserve.bootstrap.odp;

import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.estimate.ChainLadderEstimate;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.SimpleLinkRatio;
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
        //PearsonResidualClaimTriangle resSource = new PearsonResidualClaimTriangle(lrs);
        //ConstantScaleODPResidualTriangle odpResiduals = new ConstantScaleODPResidualTriangle(resSource);
        
        //DefaultResidualGenerator resGenerator = new DefaultResidualGenerator(new JavaRandom());
        //resGenerator.initialize(odpResiduals, Collections.EMPTY_LIST);
        //ResidualODPEstimateSimulator estimateSimulator = new ResidualODPEstimateSimulator(resGenerator, odpResiduals);
        
        //ODPPseudoTriangle pseudoTriangle = new ODPPseudoTriangle(resGenerator, odpResiduals);
        
        //ChainLadderEstimate estimate = new ChainLadderEstimate(new SimpleLinkRatio(new CummulatedClaimTriangle(pseudoTriangle)));
        
        estimate = new ChainLadderEstimate(lrs);
        estimate.detach();
        bootstrap = new ODPBootstrapper(estimate, N, new DummyEstimateSimulator());
    }

    @Test
    public void testCalculatePseudoReserve() {
        double[] reserves = new double[N];
        for(int i=0; i<N; i++)
            reserves[i] = bootstrap.calculatePseudoReserve();
        
        double expected = estimate.getReserve();
        for(int i=0; i<N; i++)
            assertEquals(expected, reserves[i], JRLibTestUtl.EPSILON);
        
    }
    
    private static class DummyEstimateSimulator implements ODPEstimateSimulator {
        public double simulateEstimate(double cik, int accident, int development) {
            return cik;
        }
    }

}