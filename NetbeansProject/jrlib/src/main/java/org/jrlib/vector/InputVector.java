package org.jrlib.vector;

import org.jrlib.CalculationData;

/**
 * An InputVector simply wrapes an array, to be able to use
 * it's values in further calculations.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class InputVector extends AbstractVector<CalculationData> {

    private double[] values;
    private int length;
    
    /**
     * Creates an instance for the given values. If `values` is
     * `null` then an empty vector with length of 0 is created.
     */
    public InputVector(double[] values) {
        if(values == null || values.length == 0) {
            fillEmptyData();
        } else {
            fillData(values);
        }
    }
    
    private void fillEmptyData() {
        length = 0;
    }
    
    private void fillData(double[] values) {
        length = values.length;
        this.values = new double[length];
        System.arraycopy(values, 0, this.values, 0, length);
    }
    
    /**
     * Allows extending classes to be able to reset their values after
     * initialisation. If `values` is `null` then an empty vector with 
     * length of 0 is created.
     * 
     * Calling this method fires a change event.
     */
    protected void setData(double[] data) {
        if(data == null || data.length == 0) {
            fillEmptyData();
        } else {
            fillData(data);
        }
        fireChange();
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public double getValue(int index) {
        return withinBonds(index)? values[index] : Double.NaN;
    }
    
    @Override
    protected void recalculateLayer() {
    }
}
