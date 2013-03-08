package org.jreserve.util;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface SelectableMethod<T> {

    public void fit(T source);
    
    public double getValue(int index);
}
