package org.jrlib.triangle.ratio;

/**
 * A modified claim-ratio triangle represents a triangle, which takes
 * an input {@link RatioTriangle RatioTriangle} and exposes 
 * it's values after some modifications. Examples of modifications can be:
 * -   Exluding cell(s).
 * -   Setting new values to a cell(s).
 * -   Smoothing values within the triangle.
 * 
 * Modified triangles do not change the dimensions of the source triangle, and
 * do not return non `NaN` values outside the dimensions of the source
 * triangle.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ModifiedRatioTriangle extends RatioTriangle {
    
    /**
     * Returns the ratio triangle, being modified by this instance.
     */
    public RatioTriangle getSourceRatioTriangle();
    
    @Override
    public ModifiedRatioTriangle copy();
}
