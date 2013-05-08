package org.jreserve.jrlib.bootstrap.odp.scaledresiduals;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ModifiedOdpScaledResidualTriangle extends OdpScaledResidualTriangle {
    
    /**
     * Returns the triangle being modified by this class.
     */
    public OdpScaledResidualTriangle getSourceOdpScaledResidualTriangle();
}
