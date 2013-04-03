package org.jreserve.triangle.claim;

import org.jreserve.triangle.Cell;
import org.jreserve.triangle.TriangleCorrection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimTriangleCorrection extends TriangleCorrection<ClaimTriangle> implements ModifiedClaimTriangle {

    public ClaimTriangleCorrection(ClaimTriangle source, Cell cell, double correction) {
        super(source, cell, correction);
    }
    
    public ClaimTriangleCorrection(ClaimTriangle source, int accident, int development, double correction) {
        super(source, accident, development, correction);
    }
    
    @Override
    public ClaimTriangle getSourceTriangle() {
        return source;
    }
    
    @Override
    public ClaimTriangleCorrection copy() {
        return new ClaimTriangleCorrection(
                ((ClaimTriangle)source).copy(), 
                accident, development, 
                correction);
    }
    
    @Override
    public String toString() {
        return String.format(
            "ClaimTriangleCorrection [%d; %d] = %f",
            accident, development, correction);
    }
}
