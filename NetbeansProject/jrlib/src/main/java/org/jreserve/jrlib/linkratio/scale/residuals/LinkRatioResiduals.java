package org.jreserve.jrlib.linkratio.scale.residuals;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScaleInput;
import org.jreserve.jrlib.scale.ScaleResidualTriangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;

/**
 * LinkRatioResiduals can calculate the triangle of uncorrigated 
 * pearson residuals for the link ratios.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class LinkRatioResiduals 
    extends ScaleResidualTriangle<LinkRatioScaleInput, LinkRatioScale> 
    implements LRResidualTriangle {
    
    public LinkRatioResiduals(LinkRatioScale source) {
        super(source);
    }
    
    @Override
    public LinkRatioScale getSourceLinkRatioScales() {
        return source;
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
