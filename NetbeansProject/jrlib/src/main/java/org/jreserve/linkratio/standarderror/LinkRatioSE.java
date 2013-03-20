package org.jreserve.linkratio.standarderror;

import org.jreserve.CalculationData;
import org.jreserve.Copyable;
import org.jreserve.triangle.factor.FactorTriangle;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.scale.LinkRatioScale;
import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSE extends CalculationData, Copyable<LinkRatioSE> {
    
    public LinkRatioScale getSourceLRScales();
    
    public LinkRatio getSourceLinkRatios();
    
    public FactorTriangle getSourceFactors();
    
    public ClaimTriangle getSourceTriangle();
    
    public int getDevelopmentCount();
    
    public double getValue(int development);
    
    public double[] toArray();    
}
