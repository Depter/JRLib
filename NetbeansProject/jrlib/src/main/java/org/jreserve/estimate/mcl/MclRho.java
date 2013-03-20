package org.jreserve.estimate.mcl;

import org.jreserve.CalculationData;
import org.jreserve.Copyable;
import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface MclRho extends CalculationData, Copyable<MclRho> {

    public ClaimTriangle getNumerator();
    
    public ClaimTriangle getDenominator();
    
    public int getDevelopmentCount();
    
    public double getRatio(int development);
    
    public double getRho(int development);
    
    public double[] ratiosToArray();
    
    public double[] rhosToArray();
}
