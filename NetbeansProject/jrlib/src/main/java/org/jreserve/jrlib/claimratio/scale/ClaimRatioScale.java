package org.jreserve.jrlib.claimratio.scale;

import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.scale.Scale;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangleInput;

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
     * Returns the input used to calculate the ratio triangle.
     */
    public RatioTriangleInput getSourceRatioTriangleInput();
    
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
