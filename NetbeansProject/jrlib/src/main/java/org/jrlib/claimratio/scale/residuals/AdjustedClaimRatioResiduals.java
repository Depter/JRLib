package org.jrlib.claimratio.scale.residuals;

import org.jrlib.bootstrap.AbstractAdjustedResidualTriangle;
import org.jrlib.claimratio.ClaimRatio;
import org.jrlib.claimratio.scale.ClaimRatioScale;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.ratio.RatioTriangle;

/**
 * Adjust the residuals for bootstrap-bias.
 * 
 * @see AbstractAdjustedResidualTriangle
 * @author Peter Decsi
 * @version 1.0
 */
public class AdjustedClaimRatioResiduals
    extends AbstractAdjustedResidualTriangle<CRResidualTriangle> 
    implements ModifiedCRResidualTriangle {
    
    /**
     * Creates an instance with the given source.
     * 
     * @throws NullPointerException if 'source' is null.
     */
    public AdjustedClaimRatioResiduals(ClaimRatioScale source) {
        this(new ClaimRatioResiduals(source));
    }
    
    /**
     * Creates an instance with the given source.
     * 
     * @throws NullPointerException if 'source' is null.
     */
    public AdjustedClaimRatioResiduals(CRResidualTriangle source) {
        super(source);
    }
    
    @Override
    public CRResidualTriangle getSourceResidualTriangle() {
        return source;
    }

    @Override
    public ClaimRatioScale getSourceClaimRatioScales() {
        return source.getSourceClaimRatioScales();
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
