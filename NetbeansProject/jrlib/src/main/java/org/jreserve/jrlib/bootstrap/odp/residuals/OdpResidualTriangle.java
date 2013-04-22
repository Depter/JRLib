package org.jreserve.jrlib.bootstrap.odp.residuals;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface OdpResidualTriangle extends Triangle {

    public LinkRatio getSourceLinkRatios();
    
    public ClaimTriangle getSourceTriangle();
    
    /**
     * Retunrs the incremental fitted value for the given accident and 
     * development period. If the location falls outside of the bounds, 
     * then NaN is returned.
     */
    public double getFittedValue(int accident, int development);
    
    /**
     * Returns the array, containing the incremental fitted values. 
     * The returned array should have the same location as returned by
     * {@link Triangle#toArray() toArray()}.
     */
    public double[][] toArrayFittedValues();
}
