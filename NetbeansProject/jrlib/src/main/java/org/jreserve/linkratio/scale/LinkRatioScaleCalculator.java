package org.jreserve.linkratio.scale;

import org.jreserve.linkratio.LinkRatio;
import org.jreserve.scale.RatioScaleCalculator;
import org.jreserve.triangle.claim.ClaimTriangle;
import org.jreserve.triangle.factor.FactorTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LinkRatioScaleCalculator extends RatioScaleCalculator<LinkRatioScaleInput> implements LinkRatioScale {
    
    public LinkRatioScaleCalculator(LinkRatio lrs) {
        super(new LinkRatioScaleInput(lrs));
    }
    
    public LinkRatio getSourceLinkRatio() {
        return source.getSourceLinkRatios();
    }
    
    @Override
    public LinkRatio getSourceLinkRatios() {
        return source.getSourceLinkRatios();
    }

    @Override
    public FactorTriangle getSourceFactors() {
        return source.getSourceFactors();
    }

    @Override
    public ClaimTriangle getSourceClaimTriangle() {
        return source.getSourceTriangle();
    }
}
