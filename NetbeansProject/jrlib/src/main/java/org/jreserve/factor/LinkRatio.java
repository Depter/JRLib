package org.jreserve.factor;

import org.jreserve.CalculationData;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatio extends CalculationData {
    
    /**
     * Returns the development factors, used to calculate the link ratios.
     */
    public Triangle getDevelopmentFactors();
    
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

    /**
     * Mack's alpha parameter is used to calculate the scale parameter (sigma)
     * for the variance of the link ratios.
     * 
     * <p>See {@link LinkRatioMethod#getMackAlpha() LinkRatioMethod}.</p>
     */
    public double getMackAlpha(int development);
}
