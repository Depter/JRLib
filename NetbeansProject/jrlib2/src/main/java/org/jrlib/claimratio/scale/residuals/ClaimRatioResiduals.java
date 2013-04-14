package org.jrlib.claimratio.scale.residuals;

import org.jrlib.claimratio.ClaimRatio;
import org.jrlib.claimratio.scale.ClaimRatioScale;
import org.jrlib.claimratio.scale.ClaimRatioScaleInput;
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
public class ClaimRatioResiduals 
    extends ScaleResidualTriangle<ClaimRatioScaleInput, ClaimRatioScale>
    implements CRResidualTriangle
{
    
    public ClaimRatioResiduals(ClaimRatioScale source) {
        super(source);
    }
    
    @Override
    public ClaimRatioScale getSourceClaimRatioScales() {
        return source;
    }
    
    @Override
    public ClaimRatio getSourceClaimRatios() {
        return source.getSourceClaimRatios();
    }
    
    @Override
    public RatioTriangle getSourceRatioTriangle() {
        return source.getSourceRatioTriangle();
    }
    
    @Override
    public ClaimTriangle getSourceNumeratorTriangle() {
        return source.getSourceNumeratorTriangle();
    }
    
    @Override
    public ClaimTriangle getSourceDenominatorTriangle() {
        return source.getSourceDenominatorTriangle();
    }
}