package org.jreserve.jrlib.bootstrap.mcl;

import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.util.random.Random;
import org.jreserve.jrlib.util.random.RndGamma;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleGammaMclProcessSimulator extends SimpleAbstractMclProcessSimulator {
    
    private final RndGamma rnd;
    
    public SimpleGammaMclProcessSimulator(LinkRatioScale scales, Random rnd, boolean isPaid) {
        super(scales, isPaid);
        this.rnd = new RndGamma(rnd);
    }
    
    @Override
    protected double random(double mean, double variance) {
        return rnd.nextGammaFromMeanVariance(mean, variance);
    }
}
