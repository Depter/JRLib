package org.jreserve.estimate.mcl2.ratio;

import org.jreserve.CalculationData;
import org.jreserve.Copyable;
import org.jreserve.triangle.claim.ClaimTriangle;
import org.jreserve.triangle.ratio.RatioTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Ratio extends CalculationData, Copyable<Ratio> {
    
    public RatioTriangle getSourceRatioTriangle();
    
    public ClaimTriangle getSourcePaidTriangle();
    
    public ClaimTriangle getSourceIncurredTriangle();
    
    public int getDevelopmentCount();
    
    public double getPperI(int development);
    
    public double[] toArrayPperI();
    
    public double getIperP(int development);
    
    public double[] toArrayIperP();
}
