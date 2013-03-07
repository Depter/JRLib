package org.jreserve.factor.linkratio.standarderror;

import org.jreserve.CalculationData;
import org.jreserve.factor.linkratio.scale.LinkRatioScale;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSE extends CalculationData {
    
    public LinkRatioScale getSourceLRScales();
    
    public int getDevelopmentCount();
    
    public double getValue(int development);
    
    public double[] toArray();    

}
