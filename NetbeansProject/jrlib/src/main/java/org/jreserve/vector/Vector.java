package org.jreserve.vector;

import org.jreserve.CalculationData;
import org.jreserve.Copyable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Vector extends CalculationData, Copyable<Vector> {
    
    public int getLength();
    
    public double getValue(int index);
    
    public double[] toArray();
}
