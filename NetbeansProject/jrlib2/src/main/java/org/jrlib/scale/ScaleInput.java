package org.jrlib.scale;

import org.jrlib.CalculationData;

/**
 * This interface represents the needed imput for the 
 * {@link Scale Scale} calculations. 
 * @author Peter Decsi
 * @version 1.0
 */
public interface ScaleInput extends CalculationData {

    /**
     * Returns the number of accidents period for the ratios.
     * 
     * @see Scale r(a,d).
     */
    public int getAccidentCount();
    
    /**
     * Returns the number of development periods for the ratios.
     * 
     * @see Scale r(a,d).
     */
    public int getDevelopmentCount();
    
    /**
     * Returns the number of development periods for the given
     * accident period from the ratios.
     * 
     * @see Scale r(a,d).
     */
    public int getDevelopmentCount(int accident);
    
    /**
     * Returns the estimated ratio for the given development period. If
     * the index is out of bounds, then `Double.NaN` is returned.
     * 
     * @see Scale r(d).
     */
    public double getRatio(int development);

    /**
     * Returns the estimated ratio for the given accident and development 
     * period. If the indices are out of bounds, then `Double.NaN` is 
     * returned.
     * 
     * @see Scale r(a,d).
     */
    public double getRatio(int accident, int development);
    
    /**
     * Returns the estimated ratio for the given accident and development 
     * period. If the indices are out of bounds, then `Double.NaN` is 
     * returned.
     * 
     * @see Scale w(a,d).
     */
    public double getWeight(int accident, int development);
}
