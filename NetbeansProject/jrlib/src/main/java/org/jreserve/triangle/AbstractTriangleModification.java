package org.jreserve.triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractTriangleModification<T extends Triangle> extends AbstractTriangle<T> {
    
    protected AbstractTriangleModification(T source) {
        super(source);
    }

    @Override
    public int getAccidentCount() {
        return source.getAccidentCount();
    }

    @Override
    public int getDevelopmentCount() {
        return source.getDevelopmentCount();
    }

    @Override
    public int getDevelopmentCount(int accident) {
        return source.getDevelopmentCount(accident);
    }
    
    @Override
    public double getValue(int accident, int development) {
        return source == null?
                Double.NaN :
                source.getValue(accident, development);
    }
}
