package org.jrlib.bootstrap.odp.scale;

import org.jrlib.bootstrap.odp.residuals.OdpResidualTriangle;
import org.jrlib.vector.Vector;

/**
 * Instances od OdpResidualScale can calculate the ODP scale parameter
 * for a given development period;
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface OdpResidualScale extends Vector {

   public OdpResidualTriangle getSourceResiduals(); 
}
