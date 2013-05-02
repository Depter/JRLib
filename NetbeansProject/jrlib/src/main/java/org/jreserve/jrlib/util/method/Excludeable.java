package org.jreserve.jrlib.util.method;

/**
 * This interface abstracts the capabality to include/exclude
 * specific elements from an input in a curve fitting process.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface Excludeable {
    /**
     * Excludes/Includes the element at the given index from the fitting 
     * procedure.
     */
    public void setExcluded(int index, boolean excluded);
    
    /**
     * Returns `true` if the given element is included in the
     * fitting procedure.
     */
    public boolean isExcluded(int index);    
}
