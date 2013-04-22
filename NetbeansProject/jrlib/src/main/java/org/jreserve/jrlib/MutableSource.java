package org.jreserve.jrlib;

/**
 * Classes (calculations), marked wiht this interface allow to change their
 * source data after they are initialized.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface MutableSource<T> {
    
    /**
     * Sets the source for the given isntance. It depends on the 
     * implementations, how <i>null</i> is handled (ie. throwing
     * a NullPointerException, doing noting, etc.).
     * 
     * For {@link CalculationData CalculationDatas} calling this
     * method should fire a ChangeEvent.
     */
    public void setSource(T source);
}
