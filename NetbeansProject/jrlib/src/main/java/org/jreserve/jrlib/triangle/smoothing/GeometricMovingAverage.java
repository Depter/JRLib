package org.jreserve.jrlib.triangle.smoothing;

import org.jreserve.jrlib.vector.smoothing.GeometricMovingAverageMethod;

/**
 * Smoothes the input by replacing the values by
 * their geometric moving average.
 * 
 * If `l` is the length of the moving average then the smoothed 
 * value s[i] is:
 *      s[i] = (x[i-l+1] * ... * x[l])^(1/l) , for i %gt;= l-1, and
 *      s[i] = x[i]                          , for i &lt; l-1
 * where `x[i]` is the input value at the ith position.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class GeometricMovingAverage extends AbstractVectorSmoothing {
    
    /**
     * Creates an instance for the given cells and length for the moving 
     * average.
     * 
     * @throws NullPointerException if `cells` or one of it's element is null.
     * @throws IllegalArgumentException if `length` is less then 1.
     */
    public GeometricMovingAverage(SmoothingCell[] cells, int length) {
        super(cells, new GeometricMovingAverageMethod(length));
    }
}
