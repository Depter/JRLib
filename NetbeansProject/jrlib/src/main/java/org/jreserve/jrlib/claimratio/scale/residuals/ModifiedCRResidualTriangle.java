package org.jreserve.jrlib.claimratio.scale.residuals;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ModifiedCRResidualTriangle extends CRResidualTriangle {

    /**
     * Returns the {@link CRResidualTriangle CRResidualTriangle}
     * modified by this instance.
     */
    public CRResidualTriangle getSourceResidualTriangle();
}
