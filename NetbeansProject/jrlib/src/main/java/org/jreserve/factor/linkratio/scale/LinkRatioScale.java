package org.jreserve.factor.linkratio.scale;

import org.jreserve.CalculationData;
import org.jreserve.factor.linkratio.LinkRatio;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioScale extends CalculationData {
    
    public LinkRatio getSourceLinkRatios();
    
    public int getDevelopmentCount();
    
    public double getValue(int development);
    
    public double[] toArray();    
}
