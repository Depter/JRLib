package org.jreserve.vector;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractVectorModification extends AbstractVector {

    protected AbstractVectorModification() {
    }

    protected AbstractVectorModification(Vector source) {
        super(source);
    }

    @Override
    public int getLength() {
        return source==null? 0 : source.getLength();
    }

    @Override
    public double getValue(int index) {
        return source==null?
               Double.NaN :
               source.getValue(index);
    }

}
