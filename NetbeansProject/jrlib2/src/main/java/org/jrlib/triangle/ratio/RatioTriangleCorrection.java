package org.jrlib.triangle.ratio;

import org.jrlib.triangle.Cell;
import org.jrlib.triangle.TriangleCorrection;
import org.jrlib.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RatioTriangleCorrection extends TriangleCorrection<RatioTriangle> implements ModifiedRatioTriangle {
    
    public RatioTriangleCorrection(RatioTriangle source, Cell cell, double corrigatedValue) {
        super(source, cell, corrigatedValue);
    }
    
    public RatioTriangleCorrection(RatioTriangle source, int accident, int development, double corrigatedValue) {
        super(source, accident, development, corrigatedValue);
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
    public RatioTriangleCorrection copy() {
        return new RatioTriangleCorrection(source.copy(), 
                accident, development, 
                correction);
    }
}
