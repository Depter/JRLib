package org.jrlib.util.method;

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
}
