package org.jreserve.estimate;

import org.jreserve.CalculationData;
import org.jreserve.Copyable;
import org.jreserve.triangle.Cell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Estimate extends CalculationData, Copyable<Estimate> {

    public int getAccidentCount();
    
    public int getDevelopmentCount();
    
    public int getObservedDevelopmentCount(int accident);
    
    public double getValue(Cell cell);
    
    public double getValue(int accident, int development);
    
    public double[][] toArray();
    
    public double getReserve(int accident);
    
    public double getReserve();
    
    public double[] toArrayReserve();
}
