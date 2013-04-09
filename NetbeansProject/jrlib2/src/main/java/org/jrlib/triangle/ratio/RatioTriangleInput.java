package org.jrlib.triangle.ratio;

import org.jrlib.AbstractMultiSourceCalculationData;
import org.jrlib.triangle.claim.ClaimTriangle;

/**
 * This class bundles two {@link ClaimTriangle ClaimTriangles} to be used
 * as input for a {@link RatioTriangle RatioTriangle}.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class RatioTriangleInput extends AbstractMultiSourceCalculationData<ClaimTriangle>{
    
    private ClaimTriangle numerator;
    private ClaimTriangle denominator;
    
    public RatioTriangleInput(ClaimTriangle numerator, ClaimTriangle denominator) {
        super(numerator, denominator);
        this.numerator = numerator;
        this.denominator = denominator;
    }
    
    /**
     * Returns the triangle containing the numerator claims.
     */
    public ClaimTriangle getSourceNumeratorTriangle() {
        return numerator;
    }
    
    /**
     * Returns the triangle containing the denumerator claims.
     */
    public ClaimTriangle getSourceDenominatorTriangle() {
        return denominator;
    }

    /**
     * Does nothing.
     */
    @Override
    protected void recalculateLayer() {
    }

}
