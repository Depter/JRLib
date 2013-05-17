package org.jreserve.jrlib.claimratio.scale.residuals;

import org.jreserve.jrlib.bootstrap.AbstractCenteredResidualTriangle;
import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CenteredClaimRatioResiduals 
    extends AbstractCenteredResidualTriangle<CRResidualTriangle>
    implements ModifiedCRResidualTriangle {

    public CenteredClaimRatioResiduals(CRResidualTriangle source) {
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
