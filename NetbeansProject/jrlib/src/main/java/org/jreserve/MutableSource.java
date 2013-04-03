package org.jreserve;

/**
 * Instances of MutableSource can change their input, after they
 * are initialized.
 * 
 * When the source of a {@link CalculationData CalculationData} is 
 * changed, the following should happen:
 * 
 * 1.  The calculation deregisters itself from the old source.
 * 2.  The calculation registers a listener to the new source.
 * 3.  The calculation recalculates it's state based on the new source.
 * 4.  If the calculation is not {@link CalculationData#detach() detached}, 
 *     then a change event is fired.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface MutableSource<T> {

    /**
     * Sets the new source for the calculation.
     */
    public void setSource(T source);
}
