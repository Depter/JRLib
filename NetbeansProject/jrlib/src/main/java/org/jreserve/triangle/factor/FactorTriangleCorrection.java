package org.jreserve.triangle.factor;

import org.jreserve.triangle.Cell;
import org.jreserve.triangle.TriangleCorrection;
import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FactorTriangleCorrection extends TriangleCorrection<FactorTriangle> implements ModifiedFactorTriangle {
    
    public FactorTriangleCorrection(FactorTriangle source, Cell cell, double value) {
        super(source, cell.getAccident(), cell.getDevelopment(), value);
    }
    
    public FactorTriangleCorrection(FactorTriangle source, int accident, int development, double value) {
        super(source, accident, development, value);
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
        this.source.setSource(source);
    }
    
    @Override
    protected void recalculateLayer() {
    }

    @Override
    public String toString() {
        return String.format(
            "FactorTriangleCorrection [%d; %d] = %f",
            accident, development, correction);
    }
    
    @Override
    public FactorTriangleCorrection copy() {
        return new FactorTriangleCorrection(source.copy(), 
                accident, development, 
                correction);
    }
}
