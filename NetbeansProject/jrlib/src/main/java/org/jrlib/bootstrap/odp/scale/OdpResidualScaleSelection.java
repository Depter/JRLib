package org.jrlib.bootstrap.odp.scale;

import org.jrlib.util.method.MethodSelection;

/**
 * Instances of this interface are able to extrapolate/interpolate residual
 * scales with different methods per development period.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface OdpResidualScaleSelection extends MethodSelection<OdpResidualScale, OdpRSMethod>, OdpResidualScale {
    
    /**
     * Retunrs the {@link OdpResidualScale OdpResidualScale} being 
     * extrapolated/interpolated by this instance.
     */
    public OdpResidualScale getOdpSourceResidualScale();
}