package org.jrlib.estimate;

import org.jrlib.triangle.Cell;

/**
 * The estimate interface represents the basic functionality for evry triangle
 * estimates. An estimate basically provdides a completed triangle and
 * the estimated reserve for each accident period.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface Estimate {
    
    /**
     * Returns the number of accident periods in the triangle.
     */
    public int getAccidentCount();
    
    /**
     * Returns the number of development periods in the triangle.
     */
    public int getDevelopmentCount();
    
    /**
     * Returns the number of observed development periods in the uncompleted 
     * triangle.
     */
    public int getObservedDevelopmentCount(int accident);
    
    /**
     * Returns the value from the completed triangle, represented by the 
     * given cell. If the represented coordinates fall outside the bounds,
     * then `Double.NaN` is returned.
     * 
     * @throws NullPointerException If `cell` is null.
     */
    public double getValue(Cell cell);
    
    /**
     * Returns the value from the completed triangle, represented by the 
     * given coordinates. If the represented coordinates fall outside the 
     * bounds, then `Double.NaN` is returned.
     */
    public double getValue(int accident, int development);
    
    /**
     * Creates an array from the completed triangle. Modifying the returned 
     * array does not affect the inner state of the instance. 
     * 
     * The returned array should have the same dimensions as returned
     * from the {@link #getAccidentCount() getAccidentCount} and
     * {@link #getDevelopmentCount() getDevelopmentCount} methods.
     */
    public double[][] toArray();
    
    /**
     * Returns the reserve for the given accident period.
     */
    public double getReserve(int accident);
    
    /**
     * Returns the total reserve.
     */
    public double getReserve();
    
    /**
     * Creates an array from the reserves. Modifying the returned 
     * array does not affect the inner state of the instance. 
     * 
     * The returned array should have the same dimensions as returned
     * from the {@link #getAccidentCount() getAccidentCount} method.
     */
    public double[] toArrayReserve();
}
