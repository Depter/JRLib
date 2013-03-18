package org.jreserve.bootstrap.odp;

import org.jreserve.bootstrap.ResidualTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ODPScaledResidualTriangle extends ResidualTriangle {

    public ResidualTriangle getSourceResidualTriangle();
    
    public double getScale(int development);
}
