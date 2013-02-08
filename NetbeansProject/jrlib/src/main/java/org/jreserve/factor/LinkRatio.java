package org.jreserve.factor;

import org.jreserve.CalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatio extends CalculationData {
    
    /**
     * Returns the number of development periods of the source, or
     * 0 if no source is present.
     */
    public int getDevelopmentCount();
    
    /**
     * Returns the link ratio for the given development period. If
     * <i>development &lt; 0</i> or <i>development &gt;= 
     * getDevelopmentCount()</i> then <b>Double.NaN</b> is returned.
     */
    public double getValue(int development);
    
    /**
     * Returns the link ratios as an array. The length of the returned
     * array should be equal to {@link #getDevelopmentCount() 
     * getDevelopmentCount}.
     */
    public double[] toArray();

}
