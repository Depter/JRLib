package org.jreserve.triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractTriangularModification extends AbstractTriangle {

    protected AbstractTriangularModification() {
    }

    protected AbstractTriangularModification(Triangle source) {
        super(source);
    }

    @Override
    public int getAccidentCount() {
        if(source == null)
            return 0;
        return source.getAccidentCount();
    }

    @Override
    public int getDevelopmentCount() {
        if(source == null)
            return 0;
        return source.getDevelopmentCount();
    }

    @Override
    public int getDevelopmentCount(int accident) {
        if(source == null)
            return 0;
        return source.getDevelopmentCount(accident);
    }
    
    @Override
    public double getValue(int accident, int development) {
        return source == null?
                Double.NaN :
                source.getValue(accident, development);
    }
}
