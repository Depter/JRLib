package org.jreserve.linkratio.scale;

import org.jreserve.linkratio.LinkRatio;
import org.jreserve.scale.RatioScaleEstimator;
import org.jreserve.scale.SimpleRatioScale;
import org.jreserve.triangle.claim.ClaimTriangle;
import org.jreserve.triangle.factor.FactorTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleLinkRatioScale extends SimpleRatioScale<LinkRatioScaleInput> implements LinkRatioScale {
    
    public SimpleLinkRatioScale(LinkRatio lrs) {
        super(new LinkRatioScaleCalculator(lrs));
    }
    
    public SimpleLinkRatioScale(LinkRatio lrs, RatioScaleEstimator<LinkRatioScaleInput> estimator) {
        super(new LinkRatioScaleInput(lrs), estimator);
    }
    
    @Override
    public LinkRatio getSourceLinkRatios() {
        return source.getSourceInput().getSourceLinkRatios();
    }

    @Override
    public FactorTriangle getSourceFactors() {
        return source.getSourceInput().getSourceFactors();
    }

    @Override
    public ClaimTriangle getSourceClaimTriangle() {
        return source.getSourceInput().getSourceTriangle();
    }
}
