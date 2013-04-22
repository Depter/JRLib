package org.jreserve.jrlib.bootstrap.mack;

import org.jreserve.jrlib.bootstrap.ProcessSimulator;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.util.random.Random;
import org.jreserve.jrlib.util.random.RndGamma;

/**
 * Mack process simulator, which uses a gamma distribution.
 * 
 * @see LinkRatioScale
 * @author Peter Decsi
 * @version 1.0
 */
public class MackGammaProcessSimulator extends AbstractMackProcessSimulator implements ProcessSimulator {
    
    private final RndGamma rnd;
    
    public MackGammaProcessSimulator(Random rnd, LinkRatioScale scales) {
        super(scales);
        this.rnd = new RndGamma(rnd);
    }

    @Override
    protected double random(double mean, double variance) {
        return rnd.nextGammaFromMeanVariance(mean, variance);
    }
}
