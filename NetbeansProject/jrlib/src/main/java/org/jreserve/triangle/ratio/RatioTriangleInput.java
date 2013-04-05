package org.jreserve.triangle.ratio;

import org.jreserve.triangle.TriangleOperation;
import org.jreserve.triangle.claim.ClaimTriangle;
import org.jreserve.triangle.claim.CompositeClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RatioTriangleInput extends CompositeClaimTriangle {

    private final ClaimTriangle paid;
    private final ClaimTriangle incurred;
    
    public RatioTriangleInput(ClaimTriangle paid, ClaimTriangle incurred) {
        super(paid, incurred, TriangleOperation.DIVIDE);
        this.paid = paid;
        this.incurred = incurred;
    }
    
    public ClaimTriangle getSourcePaidTriangle() {
        return paid;
    }
    
    public ClaimTriangle getSourceIncurredTriangle() {
        return incurred;
    }
    
    @Override
    public RatioTriangleInput copy() {
        return new RatioTriangleInput(paid.copy(), incurred.copy());
    }
}
