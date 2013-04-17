package org.jrlib.vector;

import org.jrlib.CalculationData;

/**
 * Vectors represent data with one dimension.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface Vector extends CalculationData {
    
    /**
     * Retunrs the length of this vector.
     */
    public int getLength();
    
    /**
     * Returns the value from the given index. If the index falls outside
     * the dimension of the vector(`0 > index || index >= length`) then
     * <i>Double.NaN</i> is returned.
     */
    public double getValue(int index);
    
    /**
     * Creates an array from the vector. Modifying the returned array does
     * not affect the inner state of the instance. 
     * 
     * The returned array should have the same dimension as returned
     * from {@link #getLength() getLength}.
     */
    public double[] toArray();
}
