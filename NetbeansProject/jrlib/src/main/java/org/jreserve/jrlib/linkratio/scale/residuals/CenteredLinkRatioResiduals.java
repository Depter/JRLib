package org.jreserve.jrlib.linkratio.scale.residuals;

import org.jreserve.jrlib.bootstrap.AbstractCenteredResidualTriangle;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CenteredLinkRatioResiduals 
    extends AbstractCenteredResidualTriangle<LRResidualTriangle>
    implements ModifiedLRResidualTriangle {

    public CenteredLinkRatioResiduals(LRResidualTriangle source) {
        super(source);
    }

    @Override
    public LRResidualTriangle getSourceLRResidualTriangle() {
        return source;
    }

    @Override
    public LinkRatioScale getSourceLinkRatioScales() {
        return source.getSourceLinkRatioScales();
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
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }
}
