package org.jreserve.estimate.mcl;

import org.jreserve.CalculationData;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface MclRho extends CalculationData {

    public Triangle getNumerator();
    
    public Triangle getDenominator();
    
    public int getDevelopmentCount();
    
    public double getRatio(int development);
    
    public double getRho(int development);
    
    public double[] ratiosToArray();
    
    public double[] rhosToArray();
}
