package org.jrlib.linkratio.scale.residuals;

import org.jrlib.linkratio.LinkRatio;
import org.jrlib.linkratio.scale.LinkRatioScale;
import org.jrlib.linkratio.scale.LinkRatioScaleInput;
import org.jrlib.scale.ScaleResidualTriangle;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.factor.FactorTriangle;

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
