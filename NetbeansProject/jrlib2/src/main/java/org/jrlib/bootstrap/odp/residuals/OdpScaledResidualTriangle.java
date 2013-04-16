package org.jrlib.bootstrap.odp.residuals;

import org.jrlib.bootstrap.odp.scale.OdpResidualScale;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface OdpScaledResidualTriangle extends ModifiedOdpResidualTriangle {
    
    /**
     * Returns the source, used to scale the input.
     */
    public OdpResidualScale getSourceOdpResidualScales();
}
