package org.jrlib.claimratio.scale.residuals;

import org.jrlib.claimratio.ClaimRatio;
import org.jrlib.claimratio.scale.ClaimRatioScale;
import org.jrlib.triangle.Triangle;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.ratio.RatioTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface CRResidualTriangle extends Triangle {

    /**
     * Returns the {@link ClaimRatioScale ClaimRatioScale} used
     * to calculate the residuals.
     */
    public ClaimRatioScale getSourceClaimRatioScales();
    
    /**
     * Returns the {@link ClaimRatio ClaimRatio} used
     * to calculate the scales.
     */
    public ClaimRatio getSourceClaimRatios();
    
    /**
     * Returns the {@link RatioTriangle RatioTriangle} used
     * to calculate the claim-ratios.
     */
    public RatioTriangle getSourceRatioTriangle();
    
    /**
     * Returns the {@link ClaimTriangle ClaimTriangle} used
     * as numerator to calculate the ratios.
     */
    public ClaimTriangle getSourceNumeratorTriangle();
    
    /**
     * Returns the {@link ClaimTriangle ClaimTriangle} used
     * as denominator to calculate the ratios.
     */
    public ClaimTriangle getSourceDenominatorTriangle();
}
