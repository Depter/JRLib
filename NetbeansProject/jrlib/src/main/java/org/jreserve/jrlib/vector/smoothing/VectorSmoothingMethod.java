package org.jreserve.jrlib.vector.smoothing;

/**
 * Implementations of this interface are able to smooth one
 * dimensional data.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface VectorSmoothingMethod {

    /**
     * Smoothes the input array.
     * 
     * @throws NullPointerException if `input` is null.
     */
    public void smooth(double[] input);
}
