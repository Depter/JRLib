package org.jrlib.linkratio.scale;

import org.jrlib.linkratio.LinkRatio;
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
public class LinkRatioResiduals extends ScaleResidualTriangle<LinkRatioScaleInput, LinkRatioScale> {
    
    public LinkRatioResiduals(LinkRatioScale source) {
        super(source);
    }
    
    public LinkRatioScale getSourceLinkRatioScales() {
        return source;
    }
    
    public LinkRatio getSourceLinkRatios() {
        return source.getSourceLinkRatios();
    }
    
    public FactorTriangle getSourceFactors() {
        return source.getSourceFactors();
    }
    
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }
}
