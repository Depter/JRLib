package org.jreserve.jrlib.bootstrap.mcl;

import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.util.random.Random;
import org.jreserve.jrlib.util.random.RndNormal;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class WeightedNormalMclProcessSimulator extends WeightedAbstractMclProcessSimulator {
    
    private final RndNormal rnd;
    
    public WeightedNormalMclProcessSimulator(LinkRatioScale scales, Random rnd, boolean isPaid) {
        super(scales, isPaid);
        this.rnd = new RndNormal(rnd);
    }
    
    @Override
    protected double random(double mean, double variance) {
        return rnd.nextNormalFromVariance(mean, variance);
    }
}
