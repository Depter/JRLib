package org.jrlib.claimratio.scale;

import org.jrlib.claimratio.ClaimRatio;
import org.jrlib.scale.ScaleResidualTriangle;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.ratio.RatioTriangle;

/**
 * ClaimRatioResiduals can calculate the triangle of uncorrigated 
 * pearson residuals for the given claim ratios.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimRatioResiduals extends ScaleResidualTriangle<ClaimRatioScaleInput, ClaimRatioScale> {
    
    public ClaimRatioResiduals(ClaimRatioScale source) {
        super(source);
    }
    
    
    public ClaimRatioScale getSourceClaimRatioScales() {
        return source;
    }
    
    public ClaimRatio getSourceClaimRatios() {
        return source.getSourceClaimRatios();
    }
    
    public RatioTriangle getSourceRatioTriangle() {
        return source.getSourceRatioTriangle();
    }
    
    public ClaimTriangle getSourceNumeratorTriangle() {
        return source.getSourceNumeratorTriangle();
    }
    
    public ClaimTriangle getSourceDenominatorTriangle() {
        return source.getSourceDenominatorTriangle();
    }
}