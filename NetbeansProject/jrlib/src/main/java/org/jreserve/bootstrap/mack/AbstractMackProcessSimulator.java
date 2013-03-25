package org.jreserve.bootstrap.mack;

import org.jreserve.linkratio.scale.LinkRatioScale;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractMackProcessSimulator {
    private final int sigmaCount;
    private final double[] sigmas;
    
    protected AbstractMackProcessSimulator(LinkRatioScale scales) {
        this.sigmas = scales.toArray();
        this.sigmaCount = this.sigmas.length;
        for(int d=0; d<sigmaCount; d++)
            sigmas[d] = Math.pow(sigmas[d], 2d);
    }
    
    protected double getVariance(double mean, int development) {
        return mean * getSigma(development);
    }
    
    protected double getSigma(int development) {
        if(0 > development)
            return Double.NaN;
        return sigmas[development >= sigmaCount? sigmaCount-1 : development];
    }

}
