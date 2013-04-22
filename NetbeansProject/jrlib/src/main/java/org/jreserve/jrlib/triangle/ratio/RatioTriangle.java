package org.jreserve.jrlib.triangle.ratio;

import org.jreserve.jrlib.MutableSource;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 * A RatioTriangle calculates the ratios from it's source 
 * numerator and denominator triangles. If the dimensions of the two 
 * input triangles does not match, than implementations should represent
 * an empty triangle (no accident periods, and development periods).
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface RatioTriangle extends Triangle, MutableSource<RatioTriangleInput> {

    /**
     * Returns the bundle used as input.
     */
    public RatioTriangleInput getSourceInput();
    
    /**
     * Returns the triangle containing the numerator claims.
     */
    public ClaimTriangle getSourceNumeratorTriangle();
    
    /**
     * Returns the triangle containing the denumerator claims.
     */
    public ClaimTriangle getSourceDenominatorTriangle();
}
