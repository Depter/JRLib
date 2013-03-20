package org.jreserve.triangle;

import org.jreserve.CalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Triangle extends CalculationData {
    
    public int getAccidentCount();
    
    public int getDevelopmentCount();
    
    public int getDevelopmentCount(int accident);
    
    public double getValue(Cell cell);
    
    public double getValue(int accident, int development);
    
    public double[][] toArray();

}
