package org.jreserve.jrlib.vector.smoothing;

import org.jreserve.jrlib.vector.Vector;

/**
 * A vector smoothing can smooth some values within a vector.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface VectorSmoothing {
    
    /**
     * Retuns the smoothed values for the given vector. The returned
     * array must have the same length as the input vector.
     * 
     * @throws NullPointerException if `input` is null.
     * @param input the vector to smooth.
     * @return the smoothed vector values.
     */
    public double[] smooth(Vector input);
}
