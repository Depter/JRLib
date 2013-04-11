package org.jrlib.triangle.ratio;

import org.jrlib.triangle.SmoothedTriangle;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.smoothing.TriangleSmoothing;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothedRatioTriangle extends SmoothedTriangle<RatioTriangle> implements ModifiedRatioTriangle {

    public SmoothedRatioTriangle(RatioTriangle source, TriangleSmoothing smoothing) {
        super(source, smoothing);
    }
    
    @Override
    public RatioTriangle getSourceRatioTriangle() {
        return source;
    }

    @Override
    public RatioTriangleInput getSourceInput() {
        return source.getSourceInput();
    }

    @Override
    public ClaimTriangle getSourceNumeratorTriangle() {
        return source.getSourceNumeratorTriangle();
    }

    @Override
    public ClaimTriangle getSourceDenominatorTriangle() {
        return source.getSourceDenominatorTriangle();
    }

    @Override
    public void setSource(RatioTriangleInput source) {
        this.source.setSource(source);
    }
    
    @Override
    public SmoothedRatioTriangle copy() {
        return new SmoothedRatioTriangle(source.copy(), smoothing.copy());
    }
}