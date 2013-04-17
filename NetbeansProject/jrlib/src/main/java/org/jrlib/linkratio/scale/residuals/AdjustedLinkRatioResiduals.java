package org.jrlib.linkratio.scale.residuals;

import org.jrlib.bootstrap.AbstractAdjustedResidualTriangle;
import org.jrlib.linkratio.LinkRatio;
import org.jrlib.linkratio.scale.LinkRatioScale;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.factor.FactorTriangle;

/**
 * Adjust the residuals for bootstrap-bias.
 * 
 * @see AbstractAdjustedResidualTriangle
 * @author Peter Decsi
 * @version 1.0
 */
public class AdjustedLinkRatioResiduals 
    extends AbstractAdjustedResidualTriangle<LRResidualTriangle> 
    implements ModifiedLRResidualTriangle {
    
    /**
     * Creates an instance with the given source.
     * 
     * @throws NullPointerException if 'source' is null.
     */
    public AdjustedLinkRatioResiduals(LinkRatioScale source) {
        this(new LinkRatioResiduals(source));
    }
    
    /**
     * Creates an instance with the given source.
     * 
     * @throws NullPointerException if 'source' is null.
     */
    public AdjustedLinkRatioResiduals(LRResidualTriangle source) {
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