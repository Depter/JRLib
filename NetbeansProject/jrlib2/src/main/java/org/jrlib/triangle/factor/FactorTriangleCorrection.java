package org.jrlib.triangle.factor;

import org.jrlib.triangle.Cell;
import org.jrlib.triangle.TriangleCorrection;
import org.jrlib.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FactorTriangleCorrection extends TriangleCorrection<FactorTriangle> 
    implements ModifiedFactorTriangle {
    
    public FactorTriangleCorrection(FactorTriangle source, Cell cell, double corrigatedValue) {
        super(source, cell, corrigatedValue);
    }
    
    public FactorTriangleCorrection(FactorTriangle source, int accident, int development, double corrigatedValue) {
        super(source, accident, development, corrigatedValue);
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

    @Override
    public FactorTriangleCorrection copy() {
        return new FactorTriangleCorrection(source.copy(), accident, development, correction);
    }
}
