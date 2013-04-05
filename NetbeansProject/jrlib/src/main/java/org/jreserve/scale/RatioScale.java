package org.jreserve.scale;

import org.jreserve.CalculationData;
import org.jreserve.Copyable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface RatioScale<T extends RatioScaleInput> extends CalculationData, Copyable<RatioScale<T>> {
    
    public T getSourceInput();
    
    public int getDevelopmentCount();
    
    public double getValue(int development);
    
    public double[] toArray();    
}
