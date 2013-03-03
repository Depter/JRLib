package org.jreserve.factor;

import org.jreserve.triangle.AbstractTriangleModification;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractFactorTriangleModification extends AbstractTriangleModification<FactorTriangle> implements FactorTriangle {

    public AbstractFactorTriangleModification(FactorTriangle source) {
        super(source);
    }
    
    @Override
    public Triangle getInputTriangle() {
        return source.getInputTriangle();
    }
}
