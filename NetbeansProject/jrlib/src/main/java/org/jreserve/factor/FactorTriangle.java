package org.jreserve.factor;

import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface FactorTriangle extends Triangle {
    
    /**
     * Returns the {@link Triangle Triangle} containing the
     * input claims for the calculations.
     */
    public Triangle getInputTriangle();
}
