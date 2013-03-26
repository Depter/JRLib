package org.jreserve.util;

import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.jreserve.bootstrap.JavaRandom;
import org.junit.Before;

/**
 *
 * @author Peter Decsi
 */
public class RndGammaTest extends AbstractRndFunctionTest {

    private final static long SEED = 1000;
    private final static double MEAN = 1500;
    private final static double VARIANCE = 100 * 100;
    
    private RndGamma rnd;
    private GammaDistribution gd;
    
    public RndGammaTest() {
    }

    @Before
    public void setUp() {
        rnd = new RndGamma(new JavaRandom(SEED));
        
        double scale = VARIANCE / MEAN;
        double shape = MEAN / scale;
        RandomGenerator rg = new JDKRandomGenerator();
        rg.setSeed(SEED);
        gd = new GammaDistribution(rg, shape, scale, GammaDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
    }

    @Override
    protected double nextRndValue() {
        return rnd.nextGammaFromMeanVariance(MEAN, VARIANCE);
    }

    @Override
    protected double getCummulativeProbability(double x) {
        return gd.cumulativeProbability(x);
    }
}