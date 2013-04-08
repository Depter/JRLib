package org.jrlib.triangle;

import org.jrlib.CalculationData;

/**
 * A triangle represents triangular data.
 * 
 * Each implementation should force the following constraints regarding
 * it's dimensions:
 * 1.  The difference between the length of accident period <i>i</i> and
 *     <i>i+1</i> shoudl be constant.
 * 2.  Accident period <i>i</i> has to be longer than accident period<i>i+1</i>.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface Triangle extends CalculationData {
    
    /**
     * Returns the number of accident periods in the triangle.
     */
    public int getAccidentCount();
    
    /**
     * Retunrs the number of development periods in the triangle. This
     * method should return the same value as <i>getDevelopmentcount(0)</i>.
     */
    public int getDevelopmentCount();
    
    /**
     * Retunrs the number of development periods for the given accident 
     * periods. For accident periods outside of the triangle, this value
     * is 0.
     */
    public int getDevelopmentCount(int accident);
    
    /**
     * Retunrs the value from the given cell. If the input represents a cell
     * outside of this triangle (both lover and upper bounds) then 
     * <i>Double.NaN</i> is returned.
     * 
     * @throws NullPointerException if <i>cell</i> is null.
     */
    public double getValue(Cell cell);
    
    /**
     * Retunrs the value from the given location. If the input represents a 
     * cell outside of this triangle (both lover and upper bounds) then 
     * <i>Double.NaN</i> is returned.
     */
    public double getValue(int accident, int development);
    
    /**
     * Creates an array from the triangle. Modifying the returned array does
     * not affect the inner state of the instance. 
     * 
     * The returned array should have the same dimensions as returned
     * from the {@link #getAccidentCount() getAccidentCount} and
     * {@link #getDevelopmentCount(int) getDevelopmentCount} methods.
     */
    public double[][] toArray();
}