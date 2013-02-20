package org.jreserve.vector;

import org.jreserve.AbstractCalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractVector extends AbstractCalculationData<Vector> implements Vector {

    protected AbstractVector(Vector source) {
        super(source);
    }

    protected AbstractVector() {
    }

    protected boolean withinBound(int index) {
        return index >= 0 &&
               index < getLength();
    }
    
    @Override
    public double[] toArray() {
        int length = getLength();
        double[] result = new double[length];
        for(int i=0; i<length; i++)
            result[i] = getValue(i);
        return result;
    }
}
