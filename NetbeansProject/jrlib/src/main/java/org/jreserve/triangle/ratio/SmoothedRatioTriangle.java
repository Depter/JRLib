package org.jreserve.triangle.ratio;

import org.jreserve.smoothing.TriangleSmoothing;
import org.jreserve.triangle.SmoothedTriangle;
import org.jreserve.triangle.claim.ClaimTriangle;

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
    public RatioTriangleInput getSourceTriangles() {
        return source.getSourceTriangles();
    }

    @Override
    public ClaimTriangle getSourcePaidTriangle() {
        return source.getSourcePaidTriangle();
    }

    @Override
    public ClaimTriangle getSourceIncurredTriangle() {
        return source.getSourceIncurredTriangle();
    }
    
    @Override
    public double getPperI(int accident, int development) {
        return getValue(accident, development);
    }
    
    @Override
    public double getIperP(int accident, int development) {
        return 1d / getValue(accident, development);
    }

    @Override
    public String toString() {
        return String.format(
            "SmoothedRatioTriangle [%s; %s]",
            source, smoothing);
    }
}
