package org.jrlib.triangle.factor;

/**
 * A modified factor triangle represents a (factor) triangle, which takes
 * an input {@link FactorTriangle FactorTriangle} and exposes it's values 
 * after some modifications. Examples of modifications can be:
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
public interface ModifiedFactorTriangle extends FactorTriangle {
    
    /**
     * Returns the source triangle, which values are being modified by this 
     * instance.
     */
    public FactorTriangle getSourceFactorTriangle();
    
    @Override
    public ModifiedFactorTriangle copy();
}
