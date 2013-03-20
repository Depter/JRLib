package org.jreserve.triangle.factor;

import org.jreserve.triangle.AbstractTriangleModification;
import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractFactorTriangleModification extends AbstractTriangleModification<FactorTriangle> implements ModifiedFactorTriangle {

    public AbstractFactorTriangleModification(FactorTriangle source) {
        super(source);
    }
    
    @Override
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }
    
    @Override
    public FactorTriangle getSourceFactors() {
        return source;
    }
    
    @Override
    public void setSource(ClaimTriangle source) {
        super.source.setSource(source);
    }
}
