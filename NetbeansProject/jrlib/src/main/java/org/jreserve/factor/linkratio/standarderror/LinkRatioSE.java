package org.jreserve.factor.linkratio.standarderror;

import org.jreserve.CalculationData;
import org.jreserve.factor.FactorTriangle;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.linkratio.scale.LinkRatioScale;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSE extends CalculationData {
    
    public LinkRatioScale getSourceLRScales();
    
    public LinkRatio getSourceLinkRatios();
    
    public FactorTriangle getSourceFactors();
    
    public Triangle getSourceTriangle();
    
    public int getDevelopmentCount();
    
    public double getValue(int development);
    
    public double[] toArray();    

}
