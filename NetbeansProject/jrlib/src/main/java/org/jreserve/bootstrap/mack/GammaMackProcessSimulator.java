package org.jreserve.bootstrap.mack;

import org.jreserve.bootstrap.ProcessSimulator;
import org.jreserve.bootstrap.Random;
import org.jreserve.linkratio.standarderror.LinkRatioScaleInput;
import org.jreserve.scale.RatioScale;
import org.jreserve.util.RndGamma;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class GammaMackProcessSimulator extends AbstractMackProcessSimulator implements ProcessSimulator {
    
    private final RndGamma rnd;
    
    public GammaMackProcessSimulator(Random rnd, RatioScale<LinkRatioScaleInput> scales) {
        super(scales);
        this.rnd = new RndGamma(rnd);
    }
    
    @Override
    public double simulateEstimate(double cik, int accident, int development) {
        double mean = cik<0d? -cik : cik;
        double var = getVariance(mean, development);
        double random = rnd.nextGammaFromMeanVariance(mean, var);
        return cik < 0d? (random + 2 * cik) : random;
    }
}
