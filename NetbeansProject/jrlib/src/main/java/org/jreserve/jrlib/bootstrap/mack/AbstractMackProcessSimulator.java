package org.jreserve.jrlib.bootstrap.mack;

import org.jreserve.jrlib.estimate.Estimate;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractMackProcessSimulator implements MackProcessSimulator {
    private final int developments;
    private final double[] scales;
    
    private Estimate estimate;
    
    protected AbstractMackProcessSimulator(LinkRatioScale scales) {
        this.developments = scales.getLength();
        this.scales = scales.toArray();
        for(int d=0; d<developments; d++)
            this.scales[d] = Math.pow(this.scales[d], 2d);
    }

    @Override
    public void setEstimate(Estimate estimate) {
        if(estimate == null)
            throw new NullPointerException("Estimate is null!");
        this.estimate = estimate;
    }
    
    @Override
    public double simulateEstimate(double cik, int accident, int development) {
        double variance = getVariance(accident, development);
        return random(cik, variance);
    }

    /**
     * Calculates the variance as `C(a,d-1) * sigma(d-1)^2`.
     * 
     * @see MackProcessSimulator
     */
    protected double getVariance(int accident, int development) {
        int d = development - 1;
        double cik = estimate.getValue(accident, d);
        double scale = getScale(d);
        return cik * scale * scale;
    }
    
    /**
     * Returns the scale parameter for the variance.
     */
    protected double getScale(int development) {
        return (0<=development && development<developments)?
                scales[development] :
                Double.NaN;
    }
    
    /**
     * Extending clases should generate a random value with the given mean
     * and variance.
     */
    protected abstract double random(double mean, double variance);
}
