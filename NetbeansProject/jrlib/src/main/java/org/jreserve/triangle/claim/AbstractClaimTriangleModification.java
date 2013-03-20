package org.jreserve.triangle.claim;

import org.jreserve.triangle.AbstractTriangleModification;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractClaimTriangleModification extends AbstractTriangleModification<ClaimTriangle> implements ModifiedClaimTriangle {

    protected AbstractClaimTriangleModification(ClaimTriangle source) {
        super(source);
    }
    
    @Override
    public ClaimTriangle getSourceTriangle() {
        return source;
    }
}
