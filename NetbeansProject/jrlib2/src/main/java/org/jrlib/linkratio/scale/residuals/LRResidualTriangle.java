package org.jrlib.linkratio.scale.residuals;

import org.jrlib.linkratio.LinkRatio;
import org.jrlib.linkratio.scale.LinkRatioScale;
import org.jrlib.triangle.Triangle;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.factor.FactorTriangle;

/**
 * LRResidualTriangle are triangles, containing scaled residuals of
 * link ratios.
 * 
 * @see org.jrlib.scale.ScaleResidualTriangle
 * @author Peter Decsi
 * @version 1.0
 */
public interface LRResidualTriangle extends Triangle {
    
    /**
     * Returns the {@link LinkRatioScale LinkRatioScale} used to
     * scale the residuals.
     */
    public LinkRatioScale getSourceLinkRatioScales();
    
    /**
     * Returns the {@link LinkRatio LinkRatios} used to
     * calculate the residuals.
     */
    public LinkRatio getSourceLinkRatios();
    
    /**
     * Returns the {@link FactorTriangle FactorTriangle} used to
     * calculate the link-ratios.
     */
    public FactorTriangle getSourceFactors();
    
    /**
     * Returns the {@link ClaimTriangle ClaimTriangle} used to
     * calculate the link-ratios.
     */
    public ClaimTriangle getSourceTriangle();
}
