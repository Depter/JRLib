package org.jreserve.bootstrap;

import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ResidualTriangle extends Triangle {

    public double getWeight(int accident, int development);
    
}
