package org.jrlib.triangle.factor;

import org.jrlib.triangle.SmoothedTriangle;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.smoothing.TriangleSmoothing;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothedFactorTriangle extends SmoothedTriangle<FactorTriangle>
implements ModifiedFactorTriangle {
    
    public SmoothedFactorTriangle(FactorTriangle source, TriangleSmoothing smoothing) {
        super(source, smoothing);
    }
        
    @Override
    public FactorTriangle getSourceFactorTriangle() {
        return source;
    }

    @Override
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }

    @Override
    public void setSource(ClaimTriangle source) {
        this.source.setSource(source);
    }
}
