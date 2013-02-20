package org.jreserve.factor.scale;

import org.jreserve.CalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioScale extends CalculationData {

    public int getDevelopmentCount();
    
    public double getValue(int development);
    
    public double[] toArray();    
}
