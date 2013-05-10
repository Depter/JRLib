package org.jreserve.jrlib.claimratio;

import org.jreserve.jrlib.MutableSource;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangleInput;
import org.jreserve.jrlib.vector.Vector;

/**
 * ClaimRatio's represent the estimated ratio between the values of two claim 
 * triangles within given development period.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface ClaimRatio extends Vector, MutableSource<RatioTriangle> {
    
    /**
     * Returns the ratio triangle used to calculate the expected ratios.
     */
    public RatioTriangle getSourceRatioTriangle();
    
    /**
     * Returns the input used to calculate the ratio triangle.
     */
    public RatioTriangleInput getSourceRatioTriangleInput();
    
    /**
     * Returns the triangle used as the numerator.
     */
    public ClaimTriangle getSourceNumeratorTriangle();
    
    /**
     * Returns the triangle used as the denominator.
     */
    public ClaimTriangle getSourceDenominatorTriangle();
}