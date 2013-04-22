package org.jreserve.jrlib.bootstrap.mack;

import org.jreserve.jrlib.bootstrap.ProcessSimulator;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.util.random.Random;
import org.jreserve.jrlib.util.random.RndNormal;

/**
 * Process simulator, which uses a normal distribution, with
 * mean equal to `cik` and variance equal to `mean * scale`.
 * 
 * @see LinkRatioScale
 * @author Peter Decsi
 * @version 1.0
 */
public class MackNormalProcessSimulator extends AbstractMackProcessSimulator implements ProcessSimulator {

    private final RndNormal rnd;
    
    public MackNormalProcessSimulator(Random rnd, LinkRatioScale scales) {
        super(scales);
        this.rnd = new RndNormal(rnd);
    }

    @Override
    protected double random(double mean, double variance) {
        return rnd.nextNormalFromVariance(mean, variance);
    }
}
