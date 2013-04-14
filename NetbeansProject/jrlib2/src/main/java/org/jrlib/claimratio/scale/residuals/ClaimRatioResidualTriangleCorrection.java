package org.jrlib.claimratio.scale.residuals;

import org.jrlib.claimratio.ClaimRatio;
import org.jrlib.claimratio.scale.ClaimRatioScale;
import org.jrlib.triangle.Cell;
import org.jrlib.triangle.TriangleCorrection;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.ratio.RatioTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimRatioResidualTriangleCorrection 
    extends TriangleCorrection<CRResidualTriangle> 
    implements ModifiedCRResidualTriangle {

    public ClaimRatioResidualTriangleCorrection(CRResidualTriangle source, Cell cell, double correction) {
        super(source, cell, correction);
    }

    public ClaimRatioResidualTriangleCorrection(CRResidualTriangle source, int accident, int development, double correction) {
        super(source, accident, development, correction);
    }
    
    public CRResidualTriangle getSourceResidualTriangle() {
        return source;
    }

    public ClaimRatioScale getSourceClaimRatioScales() {
        return source.getSourceClaimRatioScales();
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
