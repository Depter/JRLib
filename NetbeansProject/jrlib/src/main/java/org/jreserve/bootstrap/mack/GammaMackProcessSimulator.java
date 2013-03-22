package org.jreserve.bootstrap.mack;

import org.jreserve.bootstrap.ProcessSimulator;
import org.jreserve.bootstrap.Random;
import org.jreserve.linkratio.scale.LinkRatioScale;
import org.jreserve.util.RndGamma;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class GammaMackProcessSimulator implements ProcessSimulator {
    
    private final RndGamma rnd;
    private final int sigmaCount;
    private final double[] sigmas;
    
    public GammaMackProcessSimulator(Random rnd, LinkRatioScale scales) {
        this.rnd = new RndGamma(rnd);
        
        this.sigmas = scales.toArray();
        this.sigmaCount = this.sigmas.length;
        for(int d=0; d<sigmaCount; d++)
            sigmas[d] = Math.pow(sigmas[d], 2d);
    }
    
    @Override
    public double simulateEstimate(double cik, int accident, int development) {
        double mean = cik<0d? -cik : cik;
        double var = getSigma(development) * mean;
        double random = rnd.rndGammaFromMeanVariance(mean, var);
        return cik < 0d? (random + 2 * cik) : random;
    }
    
    private double getSigma(int development) {
        if(0 > development)
            return Double.NaN;
        return sigmas[development >= sigmaCount? sigmaCount-1 : development];
    }
}
