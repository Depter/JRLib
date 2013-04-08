package org.jreserve.linkratio.scale;

import org.jreserve.linkratio.LinkRatio;
import org.jreserve.scale.RatioScaleSelection;
import org.jreserve.triangle.claim.ClaimTriangle;
import org.jreserve.triangle.factor.FactorTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioScaleSelection extends LinkRatioScale, RatioScaleSelection<LinkRatioScaleInput> {
    
    public LinkRatio getSourceLinkRatios();
    
    public FactorTriangle getSourceFactors();
    
    public ClaimTriangle getSourceClaimTriangle();
}
