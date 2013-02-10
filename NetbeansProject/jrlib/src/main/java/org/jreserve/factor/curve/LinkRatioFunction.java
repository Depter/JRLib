package org.jreserve.factor.curve;

import org.jreserve.factor.LinkRatio;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioFunction {

    public void fit(LinkRatio lr);
    
    /**
     * Retunrs the smoothed link ratio for the given development period. It is
     * important, that the index here is 1 based!.
     * 
     */
    public double getValue(int development);
}
