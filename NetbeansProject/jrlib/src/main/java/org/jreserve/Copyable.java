package org.jreserve;

/**
 * A copyiable object can create a deep copy of itself.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface Copyable<T> {

    /**
     * Creates a copy of this object. The returned object should be 
     * independent from the original object. This means that all
     * fields should also be copies of the original object's members.
     */
    public T copy();
}
