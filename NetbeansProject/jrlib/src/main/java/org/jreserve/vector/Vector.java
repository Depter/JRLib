package org.jreserve.vector;

import org.jreserve.CalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Vector extends CalculationData {
    
    public int getLength();
    
    public double getValue(int index);
    
    public double[] toArray();
}
