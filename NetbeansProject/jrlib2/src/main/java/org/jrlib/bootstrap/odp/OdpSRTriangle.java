package org.jrlib.bootstrap.odp;

import org.jrlib.bootstrap.odp.scale.OdpResidualScale;
import org.jrlib.triangle.Triangle;

/**
 * Instances of OdpSRTriangle contain the scaled residuals for the ODP-bootstrap.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface OdpSRTriangle extends Triangle {

    /**
     * Returns the scale parameters used to scale the residuals.
     */
    public OdpResidualScale getSourceOdpResidualScales();
}
