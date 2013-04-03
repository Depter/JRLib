package org.jreserve.triangle.claim;

import org.jreserve.smoothing.TriangleSmoothing;
import org.jreserve.triangle.SmoothedTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothedClaimTriangle extends SmoothedTriangle<ClaimTriangle> implements ModifiedClaimTriangle {
    
    public SmoothedClaimTriangle(ClaimTriangle source, TriangleSmoothing smoothing) {
        super(source, smoothing);
    }

    @Override
    public ClaimTriangle getSourceTriangle() {
        return source;
    }
    
    @Override
    public SmoothedClaimTriangle copy() {
        return new SmoothedClaimTriangle(source.copy(), smoothing.copy());
    }

    @Override
    public String toString() {
        return String.format(
            "SmoothedClaimTriangle [%s; %s]",
            source, smoothing);
    }
}
