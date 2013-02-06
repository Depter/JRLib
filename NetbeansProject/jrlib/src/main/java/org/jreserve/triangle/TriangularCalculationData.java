package org.jreserve.triangle;

import org.jreserve.CalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangularCalculationData extends CalculationData {

    @Override
    public TriangularCalculationData getSource();
    
    public int getAccidentCount();
    
    public int getDevelopmentCount();
    
    public int getDevelopmentCount(int accident);
    
    public double getValue(int accident, int development);
    
    public double[][] toArray();
}
