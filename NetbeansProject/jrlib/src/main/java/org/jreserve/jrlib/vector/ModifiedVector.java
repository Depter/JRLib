package org.jreserve.jrlib.vector;

/**
 * A modified vector represents a vector, which takes
 * an input vector and exposes it's values after some modifications.
 * Examples of modifications can be:
 * -   Exluding cell(s).
 * -   Setting new values to a cell(s).
 * -   Smoothing values within the triangle.
 * 
 * Modified vectors do not change the dimensions of the input, and
 * do not return non <i>NaN</i> values outside the dimensions of the source
 * vector.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface ModifiedVector extends Vector {
    
    /**
     * Returns the source vector, which values are being modified by this 
     * instance.
     */
    public Vector getSourceVector();
}
