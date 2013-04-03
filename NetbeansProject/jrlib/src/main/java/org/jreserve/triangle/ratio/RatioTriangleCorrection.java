package org.jreserve.triangle.ratio;

import org.jreserve.triangle.Cell;
import org.jreserve.triangle.TriangleCorrection;
import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RatioTriangleCorrection extends TriangleCorrection<RatioTriangle> implements ModifiedRatioTriangle {
    
    public RatioTriangleCorrection(RatioTriangle source, Cell cell, double correction) {
        super(source, cell, correction);
    }
    
    public RatioTriangleCorrection(RatioTriangle source, int accident, int development, double correction) {
        super(source, accident, development, correction);
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
            "RatioTriangleCorrection [%d; %d] = %f",
            accident, development, correction);
    }
}
