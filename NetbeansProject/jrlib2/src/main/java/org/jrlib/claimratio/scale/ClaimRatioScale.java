package org.jrlib.claimratio.scale;

import org.jrlib.claimratio.ClaimRatio;
import org.jrlib.scale.Scale;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.ratio.RatioTriangle;

/**
 * The class represents the calculation of the rho parameters for the
 * Munich Chain-Ladder method.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface ClaimRatioScale extends Scale<ClaimRatioScaleInput>  {
    
    /**
     * Returns the claim-ratios used as input for the calculation.
     */
    public ClaimRatio getSourceClaimRatios();
    
    /**
     * Returns the ratio-triangle which is used to calculate the claim-ratios.
     */
    public RatioTriangle getSourceRatioTriangle();
    
    /**
     * Returns the triangle used as numerator for the ratio-triangle.
     * 
     * @see #getSourceRatioTriangle().
     */
    public ClaimTriangle getSourceNumeratorTriangle();
    
    /**
     * Returns the triangle used as denominator for the ratio-triangle.
     * 
     * @see #getSourceRatioTriangle().
     */
    public ClaimTriangle getSourceDenominatorTriangle();
}
