package org.jreserve.triangle.factor;

import org.jreserve.smoothing.TriangleSmoothing;
import org.jreserve.triangle.SmoothedTriangle;
import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothedFactorTriangle extends SmoothedTriangle<FactorTriangle> implements ModifiedFactorTriangle {
    
    public SmoothedFactorTriangle(FactorTriangle source, TriangleSmoothing smoothing) {
        super(source, smoothing);
    }

    @Override
    public FactorTriangle getSourceFactors() {
        return source;
    }

    @Override
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }

    @Override
    public void setSource(ClaimTriangle source) {
        super.source.setSource(source);
    }
    
    @Override
    public SmoothedFactorTriangle copy() {
        return new SmoothedFactorTriangle(source.copy(), smoothing.copy());
    }

    @Override
    public String toString() {
        return String.format(
            "SmoothedFactorTriangle [%s; %s]",
            source, smoothing);
    }
}
