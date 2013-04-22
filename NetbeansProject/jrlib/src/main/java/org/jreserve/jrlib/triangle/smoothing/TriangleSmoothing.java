package org.jreserve.jrlib.triangle.smoothing;

import org.jreserve.jrlib.triangle.Triangle;

/**
 * A triangle smoothing can smooth some values within a triangle.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangleSmoothing {
    
    /**
     * Retuns the smoothed values for the given triangle. The returned
     * array must have the same dimensions as the input triangle.
     * 
     * @throws NullPointerException if `input` is null.
     * @param input the triangle to smooth.
     * @return the smoothed triangle.
     */
    public double[][] smooth(Triangle input);
}
