package org.jrlib.bootstrap.odp.residuals;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ModifiedOdpResidualTriangle extends OdpResidualTriangle {

    /**
     * Retunrs the residual triangle modified by this instance.
     */
    public OdpResidualTriangle getSourceOdpResidualTriangle();
}
