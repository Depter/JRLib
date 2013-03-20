package org.jreserve.linkratio.scale;

import org.jreserve.CalculationData;
import org.jreserve.Copyable;
import org.jreserve.triangle.factor.FactorTriangle;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioScale extends CalculationData, Copyable<LinkRatioScale> {
    
    public LinkRatio getSourceLinkRatios();
    
    public FactorTriangle getSourceFactors();
    
    public ClaimTriangle getSourceTriangle();
    
    public int getDevelopmentCount();
    
    public double getValue(int development);
    
    public double[] toArray();    
}
