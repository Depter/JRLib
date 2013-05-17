package org.jreserve.jrlib.linkratio.scale.residuals;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.scale.residuals.AdjustedResidualTriangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;

/**
 * Adjust the source residual triangle.
 * 
 * @see AdjustedResidualTriangle
 * @author Peter Decsi
 * @version 1.0
 */
public class AdjustedLinkRatioResiduals 
    extends AdjustedResidualTriangle<LRResidualTriangle> 
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