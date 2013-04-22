package org.jreserve.jrlib.claimratio.scale.residuals;

import org.jreserve.jrlib.bootstrap.AbstractAdjustedResidualTriangle;
import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;

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
