package org.jreserve.bootstrap.odp;

import org.jreserve.TestData;
import org.jreserve.bootstrap.JavaRandom;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.SimpleLinkRatio;
import org.jreserve.util.MathUtil;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class GammaODPEstimateSimulatorTest {

    private final static double TOLERANCE = 0.15;
    private final static int N = 1000;
    private final static long SEED = 100;
    private ConstantScaleODPResidualTriangle residuals;
    private GammaODPEstimateSimulator simulator;
    
    public GammaODPEstimateSimulatorTest() {
    }

    @Before
    public void setUp() {
        LinkRatio lrs = new SimpleLinkRatio(TestData.getCummulatedTriangle(TestData.PAID));
        PearsonResidualClaimTriangle resSource = new PearsonResidualClaimTriangle(lrs);
        residuals = new ConstantScaleODPResidualTriangle(resSource);
        simulator = new GammaODPEstimateSimulator(new JavaRandom(SEED), residuals);
    }

    @Test
    public void testSimulateEstimate() {
        double scale = residuals.getConstantScale(); //14,893
        double mean = 500000d;
        
        double[] rnd = new double[N];
        for(int i=0; i<N; i++)
            rnd[i] = simulator.simulateEstimate(mean, 0, 0);
        
        double fMean = MathUtil.mean(rnd);
        double fVar = Math.pow(MathUtil.standardDeviation(rnd, fMean), 2d);
        double fScale = fVar / fMean;
        
        assertEquals(mean, fMean, mean * TOLERANCE);
        assertEquals(scale, fScale, scale * TOLERANCE);
    }

}