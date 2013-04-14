package org.jrlib.claimratio;

import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.ratio.RatioTriangle;
import org.jrlib.triangle.ratio.RatioTriangleInput;
import org.jrlib.vector.Vector;

/**
 * ClaimRatio's represent the estimated ratio between the values of two claim 
 * triangles within given development period.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface ClaimRatio extends Vector {
    
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
    
    @Override
    public ClaimRatio copy();
}