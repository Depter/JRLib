package org.jreserve.jrlib.util.random;

import org.jreserve.jrlib.util.random.JavaRandom;
import org.jreserve.jrlib.util.random.RndNormal;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.junit.Before;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RndNormalTest extends AbstractRndFunctionTest {
    
    private final static long SEED = 1000;
    private final static double MEAN = 1000;
    private final static double SIGMA = 100;

    private RndNormal rnd;
    private NormalDistribution nd;
    
    public RndNormalTest() {
    }

    @Before
    public void setUp() {
        rnd = new RndNormal(new JavaRandom(SEED));
        nd = new NormalDistribution(MEAN, SIGMA);
    }
    
    @Override
    protected double nextRndValue() {
        return rnd.nextNormal(MEAN, SIGMA);
    }

    @Override
    protected double getCummulativeProbability(double x) {
        return nd.cumulativeProbability(x);
    }
}
