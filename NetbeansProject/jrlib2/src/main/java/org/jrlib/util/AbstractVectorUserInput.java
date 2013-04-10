package org.jrlib.util;

import java.util.Arrays;
import org.jrlib.triangle.TriangleUtil;

/**
 * This class is the superclass for all implementations of the
 * {@link SelectableMethod SelectableMethod} interface, which 
 * purpose is to enable manual input at a given stage of a 
 * calculation.
 */
public abstract class AbstractVectorUserInput<T> implements SelectableMethod<T> {
    protected double[] values;
    
    protected AbstractVectorUserInput() {
        this.values = new double[0];
    }
    
    protected AbstractVectorUserInput(double[] values) {
        this.values = TriangleUtil.copy(values);
    }
    
    /**
     * Sets the value for the given index. If there
     * are indices before the given index, for wich
     * the value has not been set, then they have
     * the value of `NaN`.
     */
    public void setValue(int index, double value) {
        ensureIndexExists(index);
        values[index] = value;
    }
    
    private void ensureIndexExists(int development) {
        int length = values.length;
        if(length <= development)
            redimValues(development+1);
    }
    
    private void redimValues(int length) {
        int oldLength = values.length;
        double[] redim = new double[length];
        System.arraycopy(values, 0, redim, 0, oldLength);
        values = redim;
        Arrays.fill(values, oldLength, length-1, Double.NaN);
    }
    
    /**
     * Does nothing.
     */
    @Override
    public void fit(T source) {
    }
    
    /**
     * Returns the value for the given index or `NaN` if
     * no value has been set yet.
     */
    @Override
    public double getValue(int index) {
        if(index < 0 || index >= values.length)
            return Double.NaN;
        return values[index];
    }
}
