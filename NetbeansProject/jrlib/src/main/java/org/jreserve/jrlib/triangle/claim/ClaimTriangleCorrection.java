package org.jreserve.jrlib.triangle.claim;

import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.TriangleCorrection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClaimTriangleCorrection extends TriangleCorrection<ClaimTriangle> 
    implements ModifiedClaimTriangle {

    public ClaimTriangleCorrection(ClaimTriangle source, Cell cell, double corrigatedValue) {
        super(source, cell, corrigatedValue);
    }

    public ClaimTriangleCorrection(ClaimTriangle source, int accident, int development, double corrigatedValue) {
        super(source, accident, development, corrigatedValue);
    }
    
    @Override
    public ClaimTriangle getSourceClaimTriangle() {
        return source;
    }
}
