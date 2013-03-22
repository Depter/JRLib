package org.jreserve.util;

import org.jreserve.TestData;
import org.jreserve.bootstrap.JavaRandom;
import org.jreserve.bootstrap.odp.ConstantScaleODPResidualTriangle;
import org.jreserve.bootstrap.odp.GammaODPProcessSimulator;
import org.jreserve.bootstrap.odp.PearsonResidualClaimTriangle;
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
public class RndGammaTest {

    private final static double TOLERANCE = 0.15;
    private final static int N = 1000;
    private final static long SEED = 100;
    //private ConstantScaleODPResidualTriangle residuals;
    //private GammaODPEstimateSimulator simulator;
    
    private RndGamma rndGamma;
    
    public RndGammaTest() {
    }

    @Before
    public void setUp() {
        rndGamma = new RndGamma(new JavaRandom(SEED));
    }

    @Test
    public void testSimulateEstimate() {
        double scale =  15000;
        double mean = 500000d;
        double var = scale * mean;
        
        double[] rnd = new double[N];
        for(int i=0; i<N; i++)
            rnd[i] = rndGamma.rndGammaFromMeanVariance(mean, var);
        
        double fMean = MathUtil.mean(rnd);
        double fVar = Math.pow(MathUtil.standardDeviation(rnd, fMean), 2d);
        double fScale = fVar / fMean;
        
        assertEquals(mean, fMean, mean * TOLERANCE);
        assertEquals(scale, fScale, scale * TOLERANCE);
    }

}