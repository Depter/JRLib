package org.jreserve.jrlib.bootstrap.odp.scale;

import org.jreserve.jrlib.bootstrap.odp.residuals.OdpResidualTriangle;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.vector.Vector;

/**
 * Instances od OdpResidualScale can calculate the ODP scale parameter for a
 * given development period;
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface OdpResidualScale extends Vector {

    public OdpResidualTriangle getSourceOdpResidualTriangle();

    /**
     * Retunrs the incremental fitted value for the given accident and
     * development period. If the location falls outside of the bounds, then NaN
     * is returned.
     */
    public double getFittedValue(int accident, int development);

    /**
     * Returns the array, containing the incremental fitted values. The returned
     * array should have the same location as returned by
     * {@link org.jreserve.jrlib.triangle.Triangle#toArray() toArray()}
     */
    public double[][] toArrayFittedValues();

    public LinkRatio getSourceLinkRatios();

    public ClaimTriangle getSourceTriangle();
}
