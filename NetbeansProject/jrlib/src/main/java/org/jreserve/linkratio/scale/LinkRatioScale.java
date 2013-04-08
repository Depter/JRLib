package org.jreserve.linkratio.scale;

import org.jreserve.linkratio.LinkRatio;
import org.jreserve.scale.RatioScale;
import org.jreserve.triangle.claim.ClaimTriangle;
import org.jreserve.triangle.factor.FactorTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioScale extends RatioScale<LinkRatioScaleInput> {
    
    public LinkRatio getSourceLinkRatios();
    
    public FactorTriangle getSourceFactors();
    
    public ClaimTriangle getSourceClaimTriangle();
}
