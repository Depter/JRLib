package org.jreserve.triangle.factor;

import org.jreserve.Copyable;
import org.jreserve.MutableSource;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface FactorTriangle extends Triangle, MutableSource<ClaimTriangle>, Copyable<FactorTriangle> {
    
    /**
     * Returns the {@link Triangle Triangle} containing the
     * input claims for the calculations.
     */
    public ClaimTriangle getSourceTriangle();
}
