package org.jreserve.factor;

import org.jreserve.triangle.AbstractTriangleModification;
import org.jreserve.triangle.Triangle;

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
    public Triangle getSourceTriangle() {
        return source.getSourceTriangle();
    }
    
    @Override
    public FactorTriangle getSourceFactors() {
        return source;
    }
}
