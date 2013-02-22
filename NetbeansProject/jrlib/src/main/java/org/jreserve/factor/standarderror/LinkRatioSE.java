package org.jreserve.factor.standarderror;

import org.jreserve.CalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSE extends CalculationData {
    
    public LinkRatioScale getLinkRatioScales();
    
    public int getDevelopmentCount();
    
    public double getValue(int development);
    
    public double[] toArray();    

}
