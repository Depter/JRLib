package org.jrlib.util;

import org.jrlib.Copyable;

/**
 * A SelectableMethod can be used in situations, when there are more 
 * possible ways to calculate a value. Such situation can be the
 * calculation and smoothing of link-ratios, extrapolating Mack's 
 * scale factors, etc.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface SelectableMethod<T> {
    
    /**
     * Fits the method to the source data. Behaviour, when <i>source</i> is
     * <i>null</i> is undefined.
     */
    public void fit(T source);
    
    /**
     * Returns the calculated value for the given index. This method should
     * not throw any exceptions. If it is not yet initialised, or
     * <i>index</i> falls outside of some bounds, then Double.NaN should
     * be returned.
     */
    public double getValue(int index);
    
    /**
     * Creates an independent copy of this method. The contract for
     * this method is the same as for {@link Copyable#copy() Copyable}.
     * Extending interfaces can decide wether they would like to expose
     * this functionality.
     */
    public SelectableMethod<T> copy();
}
