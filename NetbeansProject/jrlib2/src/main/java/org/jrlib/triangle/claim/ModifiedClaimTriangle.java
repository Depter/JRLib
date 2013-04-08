package org.jrlib.triangle.claim;

/**
 * A modified claim triangle represents a (claim) triangle, which takes
 * an input (claim) triangle and exposes it's values after some modifications.
 * Examples of modifications can be:
 * -   Exluding cell(s).
 * -   Setting new values to a cell(s).
 * -   Smoothing values within the triangle.
 * 
 * Modified triangles do not change the dimensions of the triangle, and
 * do not return non <i>NaN</i> values outside the dimensions of the source
 * triangle.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface ModifiedClaimTriangle extends ClaimTriangle {

    /**
     * Returns the source triangle, which values are being modified by this 
     * instance.
     */
    public ClaimTriangle getSourceClaimTriangle();
    
    @Override
    public ModifiedClaimTriangle copy();
}
