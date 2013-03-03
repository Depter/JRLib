package org.jreserve.smoothing;

import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangleSmoothing {
    /**
     * Retuns the smoothed values for the given triangle.
     * 
     * @param input the triangle to smooth.
     * @return the smoothed triangle.
     */
    public double[][] smooth(Triangle input);
}
