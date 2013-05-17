package org.jreserve.jrlib.claimratio.scale.residuals;

import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScaleInput;
import org.jreserve.jrlib.scale.residuals.ScaleResidualTriangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;

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