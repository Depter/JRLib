package org.jrlib.linkratio.scale;

import org.jrlib.Copyable;
import org.jrlib.linkratio.LinkRatio;
import org.jrlib.scale.Scale;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.factor.FactorTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioScale extends Scale<LinkRatioScaleInput>, Copyable<LinkRatioScale> {
    
    public LinkRatio getSourceLinkRatios();
    
    public FactorTriangle getSourceFactors();
    
    public ClaimTriangle getSourceTriangle();
}
