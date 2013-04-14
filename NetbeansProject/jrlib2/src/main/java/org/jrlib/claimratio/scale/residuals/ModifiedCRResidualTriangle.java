package org.jrlib.claimratio.scale.residuals;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ModifiedCRResidualTriangle extends CRResidualTriangle {

    /**
     * Returns the {@link ClaimRatioResidualTriangle ClaimRatioResidualTriangle}
     * modified by this instance.
     */
    public CRResidualTriangle getSourceResidualTriangle();
}
