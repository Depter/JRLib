package org.jrlib.triangle.claim;

import org.jrlib.triangle.SmoothedTriangle;
import org.jrlib.triangle.smoothing.TriangleSmoothing;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothedClaimTriangle extends SmoothedTriangle<ClaimTriangle> 
    implements ModifiedClaimTriangle {

    public SmoothedClaimTriangle(ClaimTriangle source, TriangleSmoothing smoothing) {
        super(source, smoothing);
    }
    
    @Override
    public ClaimTriangle getSourceClaimTriangle() {
        return source;
    }

    @Override
    public ModifiedClaimTriangle copy() {
        return new SmoothedClaimTriangle(source.copy(), smoothing.copy());
    }

}
