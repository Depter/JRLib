package org.jreserve.jrlib.triangle.smoothing;

import org.jreserve.jrlib.vector.smoothing.ExponentialSmoothingMethod;

/**
 * Smoothies the values using exponential smoothing.
 * 
 * The class creates the smoothed values `s[i]` from input values
 * `x[i]`-s as follows:
 *     s[0] = x[0]
 *     s[i|i>0] = alpha * x[i] + (1-alpha) * s[i-1]
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class ExponentialSmoothing extends AbstractVectorSmoothing {
    
    /**
     * Creates an instance for the given cells and alpha parameter.
     * 
     * @throws NullPointerException if `cells` or one of it's element is null.
     * @throws IllegalArgumentException if `alpha` is  not in the `[0;1]` 
     * interval.
     */
    public ExponentialSmoothing(SmoothingCell[] cells, double alpha) {
        super(cells, new ExponentialSmoothingMethod(alpha));
    }
}
