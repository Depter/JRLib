package org.jreserve.jrlib.vector;

import org.jreserve.jrlib.AbstractCalculationData;
import org.jreserve.jrlib.CalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractVector<T extends CalculationData> extends AbstractCalculationData<T> implements Vector {
    
    /**
     * Creates a class, with the given source.
     * 
     * @throws NullPointerException when `source` is `null`.
     */
    protected AbstractVector(T source) {
        super(source);
    }
    
    /**
     * Creates an instance, without a source calculation.
     */
    protected AbstractVector() {
    }

    /**
     * Returns `true` if the given index is within the bounds of this vector.
     */
    protected boolean withinBonds(int index) {
        return 0 <= index && index < getLength();
    }
    
    @Override
    public double[] toArray() {
        int size = getLength();
        double[] result = new double[size];
        for(int i=0; i<size; i++)
            result[i] = getValue(i);
        return result;
    }

}
