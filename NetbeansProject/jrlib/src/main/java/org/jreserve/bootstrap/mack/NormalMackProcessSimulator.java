package org.jreserve.bootstrap.mack;

import org.jreserve.bootstrap.ProcessSimulator;
import org.jreserve.bootstrap.Random;
import org.jreserve.linkratio.standarderror.LinkRatioScaleInput;
import org.jreserve.scale.RatioScale;
import org.jreserve.util.RndNormal;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class NormalMackProcessSimulator extends AbstractMackProcessSimulator implements ProcessSimulator {

    private final RndNormal rnd;
    
    public NormalMackProcessSimulator(Random rnd, RatioScale<LinkRatioScaleInput> scales) {
        super(scales);
        this.rnd = new RndNormal(rnd);
    }
    
    @Override
    public double simulateEstimate(double cik, int accident, int development) {
        double var = super.getVariance(cik, development);
        return rnd.nextNormalFromVariance(cik, var);
    }
}